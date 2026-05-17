package mx.izzi.offboarding.modules.employees.services;

import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAllEmployeesByImmediateBoss(Long idssff);
    Optional<Employee> findOneBy(Long idssff);
    Page<Employee> findAll(int page, int size);
}
