package mx.izzi.offboarding.modules.terminations.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.employees.services.EmployeeService;
import mx.izzi.offboarding.modules.terminations.domain.dtos.CreateAccessBlockRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.entities.AccessBlockRequestEntity;
import mx.izzi.offboarding.modules.terminations.domain.models.AccessBlockRequest;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationMapper;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationReason;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationType;
import mx.izzi.offboarding.modules.terminations.repositories.AccessBlockingRequestRepository;
import mx.izzi.offboarding.modules.terminations.services.TerminationReasonService;
import mx.izzi.offboarding.modules.terminations.services.TerminationService;
import mx.izzi.offboarding.modules.terminations.services.TerminationTypeService;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.domain.mappers.UserMapper;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.shared.enums.EmployeeStatus;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TerminationServiceImp implements TerminationService {

    private final String PATH = "/api/v1/terminations";

    private final AccessBlockingRequestRepository accessBlockingRequestRepository;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final TerminationTypeService terminationTypeService;
    private final TerminationReasonService terminationReasonService;

    @Override
    public Optional<AccessBlockRequest> findOneBy(String folio) {
        Optional<AccessBlockRequest> request = this.accessBlockingRequestRepository
                .findByFolio(folio)
                .map(AccessBlockRequestEntity::toDomain);

        if  (request.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + folio);
        }

        if (request.get().getIsActive().equals(false)) {
            throw new ResourceNotFoundException(PATH + "/" + folio);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        boolean isSameUser = userDetails.getUsername().equals(request.get().getUser().getIdssff().toString());

        if  (!isSameUser) {
            throw new ResourceAccessDeniedException(PATH + "/" + folio);
        }

        return request;
    }

    @Override
    public Page<AccessBlockRequest> findAllTerminationsByUserId(Long userIdssff, int page, int size) {
        Optional<User> user = this.userService.findOneBy(userIdssff);

        if (user.isEmpty()) {
            throw new ServerException("/api/v1/users" + "/" + userIdssff + "/terminations");
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        boolean isSameUser = userDetails.getUsername().equals(userIdssff.toString());

        if  (!isSameUser) {
            throw new ResourceAccessDeniedException("/api/v1/users" + "/" + userIdssff + "/employees");
        }

        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }

        Pageable pageable = PageRequest.of(page, size);

        return this.accessBlockingRequestRepository
                .findByUser(pageable, UserMapper.toEntity(user.get()))
                .map(AccessBlockRequestEntity::toDomain);
    }

    @Transactional
    @Override
    public Optional<AccessBlockRequest> create(CreateAccessBlockRequestDto dto) {

        Optional<User> user = this.userService.findOneBy(Long.parseLong(dto.getUserIdssff()));

        if (user.isEmpty()) {
            throw new ServiceException(PATH);
        }

        Optional<Employee> employee = this.employeeService.findOneBy(Long.parseLong(dto.getEmployeeIdssff()));

        if (employee.isEmpty()) {
            throw new ServiceException(PATH);
        }

        if (employee.get().getStatus().equals(EmployeeStatus.TERMINATED.getName())) {
            throw new ResourceAlreadyTerminatedException(PATH);
        }

        if (!employee.get().getImmediateBoos().getIdssff().equals(user.get().getIdssff())) {
            throw new ResourceAccessDeniedException(PATH);
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
                .user(user.get())
                .employee(employee.get())
                .terminationType(type.get())
                .terminationReason(reason.get())
                .build();

        employee.get().setStatus("TERMINATED");

        Optional<Employee> employeeToUpdate = this.employeeService
                .update(employee.get().getIdssff(), EmployeeMapper.toDto(employee.get()));

        if (employeeToUpdate.isEmpty()) {
            throw new ServiceException(PATH);
        }

        return Optional.of(
                this.accessBlockingRequestRepository
                        .saveAndFlush(TerminationMapper.toEntity(accessBlockRequest))
                        .toDomain()
        );
    }
}
