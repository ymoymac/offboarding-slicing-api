package mx.izzi.offboarding.modules.employees.services;

import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public List<Employee> findAllByImmediateBoss(Long idssff) {
        return List.of();
    }
}
