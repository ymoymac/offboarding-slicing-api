package mx.izzi.offboarding.modules.employees.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.izzi.offboarding.modules.auth.domain.models.AuthUtils;
import mx.izzi.offboarding.modules.employees.domain.dtos.UpdateEmployeeDto;
import mx.izzi.offboarding.modules.employees.domain.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.employees.repositories.EmployeeRepository;
import mx.izzi.offboarding.modules.employees.services.EmployeeService;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.domain.mappers.UserMapper;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;
import mx.izzi.offboarding.modules.workcenter.repositories.WorkCenterRepository;
import mx.izzi.offboarding.shared.enums.Roles;
import mx.izzi.offboarding.shared.exceptions.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final String PATH = "/api/v1/employees";

    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final WorkCenterRepository workCenterRepository;

    @Override
    public List<Employee> findAllEmployeesByImmediateBoss(Long immediateBossIdssff) {

        Optional<User> immediateBoss = this.userService.findOneBy(immediateBossIdssff);

        if  (immediateBoss.isEmpty()) {
            throw new ServerException("/api/v1/users" + "/" + immediateBossIdssff + "/employees");
        }

        if  (!AuthUtils.isSameUser(immediateBoss.get())) {
            throw new ResourceAccessDeniedException("/api/v1/users" + "/" + immediateBossIdssff + "/employees");
        }

        return this.employeeRepository.findByImmediateBoss(UserMapper.toEntity(immediateBoss.get()))
                .stream()
                .map(EmployeeEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Employee> findOneEmployeeByImmediateBoss(Long immediateBossIdssff, Long idssff) {

        log.info("[INFO] execute findOneEmployeeByImmediateBoss");
        UserDetails currentUser = AuthUtils.getCurrentUser();
        Optional<User> immediateBoss = this.userService.findOneBy(immediateBossIdssff);

        log.info("[INFO] ImmediateBoss: {}", immediateBoss);

        if  (immediateBoss.isEmpty()) {
            throw new ServerException("/api/v1/users" + "/" + immediateBossIdssff + "/employee/" + idssff);
        }

        Optional<Employee> employeeFound = this.findOneBy(idssff);

        log.info("[INFO] Employee: {}", employeeFound);

        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Roles.RRHH.getName()))) {
            return employeeFound;
        }

        if  (!AuthUtils.isSameUser(immediateBoss.get())) {
            throw new ResourceAccessDeniedException("/api/v1/users" + "/" + immediateBossIdssff + "/employee/" + idssff);
        }

        Optional<Employee> employee = this.employeeRepository
                .findOneByImmediateBoss(idssff, UserMapper.toEntity(immediateBoss.get()))
                .map(EmployeeEntity::toDomain);

        if (employee.isEmpty()) {
            throw new ResourceOutsideOfStructureException("/api/v1/users" + "/" + idssff + "/employee/");
        }

        return employee;
    }

    @Override
    public Optional<Employee> findOneBy(Long idssff) {

        log.info("[INFO] execute findOneBy employee");

        Optional<Employee> employee = this.employeeRepository
                .findOneByIdssff(idssff)
                .map(EmployeeEntity::toDomain);

        log.info("[INFO] Employee found: {}", employee);

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

        log.info("[INFO] execute update employee: {}", updateEmployeeDto);

        Optional<Employee> employeeFound = this.findOneBy(idssff);
        if (employeeFound.isEmpty()) {
            throw new ServerException(PATH);
        }

        Optional<UserEntity> immediateBoss = this.userRepository
                .findOneByIdssff(employeeFound.get().getImmediateBoos().getIdssff());

        if (immediateBoss.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + idssff);
        }

        Optional<WorkCenterEntity> workCenterEntity = this.workCenterRepository
                .findById(employeeFound.get().getWorkCenter().getId());

        if  (workCenterEntity.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + idssff);
        }

        UserDetails userDetails = AuthUtils.getCurrentUser();

        Employee employee = Employee.builder()
                .employeeId(employeeFound.get().getEmployeeId())
                .idssff(employeeFound.get().getIdssff())
                .number(employeeFound.get().getNumber())
                .name(employeeFound.get().getName())
                .firstSurname(employeeFound.get().getFirstSurname())
                .secondSurname(employeeFound.get().getSecondSurname())
                .email(employeeFound.get().getEmail())
                .status(updateEmployeeDto.getStatus())
                .jobPositionId(employeeFound.get().getJobPositionId())
                .jobPosition(employeeFound.get().getJobPosition())
                .positionId(employeeFound.get().getPositionId())
                .position(employeeFound.get().getPosition())
                .isActive(employeeFound.get().getIsActive())
                .createdAt(employeeFound.get().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .createdBy(employeeFound.get().getCreatedBy())
                .updatedBy(userDetails.getUsername())
                .immediateBoos(employeeFound.get().getImmediateBoos())
                .workCenter(employeeFound.get().getWorkCenter())
                .build();

        log.info("[INFO] Employee updated: {}", employee);

        EmployeeEntity employeeEntity = EmployeeMapper.toEntity(employee, immediateBoss.get(), workCenterEntity.get());
        return Optional.of(this.employeeRepository.save(employeeEntity).toDomain());
    }
}
