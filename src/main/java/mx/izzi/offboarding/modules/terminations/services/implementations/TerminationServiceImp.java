package mx.izzi.offboarding.modules.terminations.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.izzi.offboarding.modules.auth.domain.models.AuthUtils;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.employees.services.EmployeeService;
import mx.izzi.offboarding.modules.terminations.domain.dtos.CreateAccessBlockRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.entities.AccessBlockRequestEntity;
import mx.izzi.offboarding.modules.terminations.domain.models.AccessBlockRequest;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationMapper;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationReason;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationType;
import mx.izzi.offboarding.modules.terminations.repositories.AccessBlockRequestRepository;
import mx.izzi.offboarding.modules.terminations.services.TerminationReasonService;
import mx.izzi.offboarding.modules.terminations.services.TerminationService;
import mx.izzi.offboarding.modules.terminations.services.TerminationTypeService;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.Role;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.domain.mappers.UserMapper;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.shared.enums.EmployeeStatus;
import mx.izzi.offboarding.shared.enums.Roles;
import mx.izzi.offboarding.shared.exceptions.*;
import mx.izzi.offboarding.shared.utils.DynFolio;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminationServiceImp implements TerminationService {

    private final String PATH = "/api/v1/terminations";

    private final AccessBlockRequestRepository accessBlockRequestRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final TerminationTypeService terminationTypeService;
    private final TerminationReasonService terminationReasonService;

    @Override
    public Optional<AccessBlockRequest> findOneBy(String folio) {
        Optional<AccessBlockRequest> request = this.accessBlockRequestRepository
                .findByFolio(folio)
                .map(AccessBlockRequestEntity::toDomain);

        if  (request.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + folio);
        }

        if (request.get().getIsActive().equals(false)) {
            throw new ResourceNotFoundException(PATH + "/" + folio);
        }

        return request;
    }

    @Override
    public Page<AccessBlockRequest> findAllTerminationsByImmediateBoss(Long immediateBossIdssff, int page, int size) {
        Optional<User> user = this.userService.findOneBy(immediateBossIdssff);

        if (user.isEmpty()) {
            throw new ServerException("/api/v1/users" + "/" + user + "/terminations");
        }

        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<AccessBlockRequest> terminations= this.accessBlockRequestRepository
                .findByImmediateBoss(pageable, UserMapper.toEntity(user.get()))
                .map(AccessBlockRequestEntity::toDomain);
        if (terminations.isEmpty()) {
            terminations = this.accessBlockRequestRepository
                    .findByUser(pageable, user.get().getIdssff())
                    .map(AccessBlockRequestEntity::toDomain);
        }

        return terminations;
    }

    @Transactional
    @Override
    public Optional<AccessBlockRequest> create(CreateAccessBlockRequestDto dto) {

        log.info("[INFO] Creating access block request: {}", dto);

        UserDetails currentUser = AuthUtils.getCurrentUser();

        Optional<User> user = this.userService.findOneByEmail(currentUser.getUsername());
        if (user.isEmpty()) {
            throw new ServiceException(PATH);
        }
        log.info("[INFO] User: {}", user);

        Optional<Employee> employee = this.employeeService.findOneBy(dto.getEmployeeIdssff());
        if (employee.isEmpty()) {
            throw new ServiceException(PATH);
        }
        log.info("[INFO] Employee: {}", employee);

        if (employee.get().getStatus().equals(EmployeeStatus.TERMINATED.getName())) {
            throw new ResourceAlreadyTerminatedException(PATH);
        }

        List<String> roleNames = user.get().getRoles().stream().map(Role::getName).toList();
        log.info("[INFO] Roles: {}", roleNames);

        if (roleNames.contains(Roles.RRHH.getName())) {
            if (!employee.get().getImmediateBoos().getIdssff().equals(dto.getImmediateBossIdssff())) {
                throw new ResourceAccessDeniedException(PATH);
            }
        }

        Optional<TerminationType> type = this.terminationTypeService.findOneBy(dto.getTerminationTypeId());

        if (type.isEmpty()) {
            throw new ServiceException(PATH);
        }

        Optional<TerminationReason> reason = this.terminationReasonService.findOneBy(dto.getTerminationReasonId());

        if (reason.isEmpty()) {
            throw new ServiceException(PATH);
        }

        LocalDateTime now = LocalDateTime.now();
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        AccessBlockRequest accessBlockRequest = AccessBlockRequest.builder()
                .folio(DynFolio.folio())
                .endDate(dto.getEndDate())
                .applicationDate(now)
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy(userDetails.getUsername())
                .updatedBy(userDetails.getUsername())
                .employee(employee.get())
                .terminationType(type.get())
                .terminationReason(reason.get())
                .build();
        log.info("[INFO] Access block request: {}", accessBlockRequest);

        employee.get().setStatus(EmployeeStatus.TERMINATED.getName());

        Optional<Employee> employeeToUpdate = this.employeeService
                .update(employee.get().getIdssff(), EmployeeMapper.toUpdateDto(employee.get()));

        if (employeeToUpdate.isEmpty()) {
            throw new ServiceException(PATH);
        }

        Optional<UserEntity> userEntity = this.userRepository.findById(user.get().getUserId());
        if (userEntity.isEmpty()) {
            throw new ResourceNotFoundException(PATH);
        }
        log.info("[INFO] Applicant User: {}", userEntity.get());

        Optional<UserEntity> immediateBoss = Optional.empty();
        if (dto.getImmediateBossIdssff() != null && !Objects.equals(user.get().getIdssff(), dto.getImmediateBossIdssff())) {
            immediateBoss = this.userRepository.findOneByIdssff(dto.getImmediateBossIdssff());
        }
        log.info("[INFO] Immediate Boss: {}", immediateBoss);

        AccessBlockRequestEntity requestEntity = TerminationMapper.toEntity(accessBlockRequest, userEntity.get(), immediateBoss.orElseGet(userEntity::get));

        return Optional.of(
                this.accessBlockRequestRepository
                        .saveAndFlush(requestEntity)
                        .toDomain()
        );
    }
}
