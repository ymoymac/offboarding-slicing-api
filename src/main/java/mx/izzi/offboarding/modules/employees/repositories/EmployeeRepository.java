package mx.izzi.offboarding.modules.employees.repositories;

import mx.izzi.offboarding.modules.employees.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    EmployeeEntity findByImmediateBoss(UserEntity immediateBoss);
}
