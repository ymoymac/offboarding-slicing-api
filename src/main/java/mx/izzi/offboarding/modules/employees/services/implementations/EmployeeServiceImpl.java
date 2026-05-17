package mx.izzi.offboarding.modules.employees.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.employees.domain.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.employees.repositories.EmployeeRepository;
import mx.izzi.offboarding.modules.employees.services.EmployeeService;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.domain.models.UserMapper;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.shared.exceptions.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final String PATH = "/api/v1/employees";

    private final EmployeeRepository employeeRepository;
    private final UserService userService;

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
            throw new ValueNotValidException(PATH);
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.employeeRepository.findAllActiveEmployees(pageable).map(EmployeeEntity::toDomain);
    }
}
