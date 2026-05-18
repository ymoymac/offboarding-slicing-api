package mx.izzi.offboarding.modules.terminations.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.employees.services.EmployeeService;
import mx.izzi.offboarding.modules.terminations.domain.dtos.CreateAccessBlockingRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.models.AccessBlockingRequest;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationMapper;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationReason;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationType;
import mx.izzi.offboarding.modules.terminations.repositories.AccessBlockingRequestRepository;
import mx.izzi.offboarding.modules.terminations.services.TerminationReasonService;
import mx.izzi.offboarding.modules.terminations.services.TerminationService;
import mx.izzi.offboarding.modules.terminations.services.TerminationTypeService;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.shared.enums.EmployeeStatus;
import mx.izzi.offboarding.shared.exceptions.ResourceAccessDeniedException;
import mx.izzi.offboarding.shared.exceptions.ResourceAlreadyTerminatedException;
import mx.izzi.offboarding.shared.utils.DynFolio;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    @Override
    public Optional<AccessBlockingRequest> create(CreateAccessBlockingRequestDto dto) {

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

        //verificar que el usuario tenga relacion con el empleado

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

        AccessBlockingRequest accessBlockingRequest = AccessBlockingRequest.builder()
                .folio(DynFolio.folio())
                .endDate(dto.getEndDate())
                .applicationDate(now)
                .laboraId(dto.getLaboraId())
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
                        .saveAndFlush(TerminationMapper.toEntity(accessBlockingRequest))
                        .toDomain()
        );
    }
}
