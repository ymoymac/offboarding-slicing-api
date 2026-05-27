package mx.izzi.offboarding.modules.employees.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.employees.domain.dtos.UpdateEmployeeDto;
import mx.izzi.offboarding.modules.employees.domain.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.employees.repositories.EmployeeRepository;
import mx.izzi.offboarding.modules.employees.services.EmployeeService;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.domain.mappers.UserMapper;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;
import mx.izzi.offboarding.modules.workcenter.services.WorkCenterService;
import mx.izzi.offboarding.shared.exceptions.*;
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
public class EmployeeServiceImpl implements EmployeeService {

    private final String PATH = "/api/v1/employees";

    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final WorkCenterService workCenterService;

    @Override
    public List<Employee> findAllEmployeesByImmediateBoss(Long immediateBossIdssff) {

        Optional<User> immediateBoss = this.userService.findOneBy(immediateBossIdssff);

        if  (immediateBoss.isEmpty()) {
            throw new ServerException("/api/v1/users" + "/" + immediateBossIdssff + "/employees");
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        boolean isSameUser = userDetails.getUsername().equals(immediateBossIdssff.toString());

        if  (!isSameUser) {
            throw new ResourceAccessDeniedException("/api/v1/users" + "/" + immediateBossIdssff + "/employees");
        }

        return this.employeeRepository.findByImmediateBoss(UserMapper.toEntity(immediateBoss.get()))
                .stream()
                .map(EmployeeEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Employee> findOneEmployeeByImmediateBoss(Long immediateBossIdssff, Long idssff) {
        Optional<User> immediateBoss = this.userService.findOneBy(immediateBossIdssff);

        if  (immediateBoss.isEmpty()) {
            throw new ServerException("/api/v1/users" + "/" + immediateBossIdssff + "/employee/" + idssff);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        boolean isSameUser = userDetails.getUsername().equals(immediateBossIdssff.toString());

        if  (!isSameUser) {
            throw new ResourceAccessDeniedException("/api/v1/users" + "/" + immediateBossIdssff + "/employee/" + idssff);
        }
        Optional<Employee> employeeFound = this.employeeRepository
                .findOneByImmediateBoss(idssff, UserMapper.toEntity(immediateBoss.get()))
                .map(EmployeeEntity::toDomain);

        if (employeeFound.isEmpty()) {
            throw new ResourceNotFoundException("/api/v1/users" + "/" + idssff + "/employee/" + idssff);
        }

        if (employeeFound.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException("/api/v1/users" + "/" + idssff + "/employee/" + idssff);
        }

        return employeeFound;
    }

    @Override
    public Optional<Employee> findOneBy(Long idssff) {
        Optional<Employee> employee = this.employeeRepository
                .findOneByIdssff(idssff)
                .map(EmployeeEntity::toDomain);

        if (employee.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + idssff);
        }

        if (employee.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH + "/" + idssff);
        }

        return employee;
    }

    @Override
    public Page<Employee> findAll(int page, int size) {
        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.employeeRepository
                .findAllActiveEmployees(pageable)
                .map(EmployeeEntity::toDomain);
    }

    @Transactional
    @Override
    public Optional<Employee> update(Long idssff, UpdateEmployeeDto updateEmployeeDto) {
        WorkCenter workCenter = null;

        Optional<Employee> employeeFound = this.findOneBy(idssff);
        if (employeeFound.isEmpty()) {
            throw new ServerException(PATH);
        }

        if (updateEmployeeDto.getWorkCenterId() != null) {
            Optional<WorkCenter> workCenterFound = this.workCenterService.findOneBy(updateEmployeeDto.getWorkCenterId());

            if (workCenterFound.isEmpty()) {
                throw new ResourceNotFoundException(PATH + "/" + idssff);
            }

            workCenter = workCenterFound.get();
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Employee employee = Employee.builder()
                .employeeId(employeeFound.get().getEmployeeId())
                .idssff(employeeFound.get().getIdssff())
                .number(updateEmployeeDto.getNumber() != null ?  updateEmployeeDto.getNumber() : employeeFound.get().getNumber())
                .name(employeeFound.get().getName())
                .firstSurname(employeeFound.get().getFirstSurname())
                .secondSurname(employeeFound.get().getSecondSurname())
                .email(updateEmployeeDto.getEmail() != null ? updateEmployeeDto.getEmail() : employeeFound.get().getEmail())
                .status(updateEmployeeDto.getStatus() != null ? updateEmployeeDto.getStatus() : employeeFound.get().getStatus())
                .jobPositionId(updateEmployeeDto.getJobPositionId() !=  null ? updateEmployeeDto.getJobPositionId() : employeeFound.get().getJobPositionId())
                .jobPosition(updateEmployeeDto.getJobPosition() != null ? updateEmployeeDto.getJobPosition() : employeeFound.get().getJobPosition())
                .positionId(updateEmployeeDto.getPositionId() != null ? updateEmployeeDto.getPositionId() : employeeFound.get().getPositionId())
                .position(updateEmployeeDto.getPosition() != null ? updateEmployeeDto.getPosition() : employeeFound.get().getPosition())
                .isActive(employeeFound.get().getIsActive())
                .createdAt(employeeFound.get().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .createdBy(employeeFound.get().getCreatedBy())
                .updatedBy(userDetails.getUsername())
                .immediateBoos(employeeFound.get().getImmediateBoos())
                .workCenter(workCenter)
                .build();

        return Optional.of(this.employeeRepository.save(EmployeeMapper.toEntity(employee)).toDomain());
    }
}
