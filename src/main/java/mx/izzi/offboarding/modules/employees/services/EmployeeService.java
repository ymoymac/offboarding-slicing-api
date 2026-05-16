package mx.izzi.offboarding.modules.employees.services;

import mx.izzi.offboarding.modules.employees.models.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAllByImmediateBoss(Long idssff);
}
