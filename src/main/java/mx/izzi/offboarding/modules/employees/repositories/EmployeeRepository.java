package mx.izzi.offboarding.modules.employees.repositories;

import mx.izzi.offboarding.modules.employees.domain.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    @Query("""
        SELECT e FROM EmployeeEntity e
        JOIN FETCH e.workCenter
        WHERE e.immediateBoss = :immediateBossIdssff
        AND e.isActive = true
    """)
    List<EmployeeEntity> findByImmediateBoss(UserEntity immediateBossIdssff);
    @Query("SELECT e FROM EmployeeEntity e JOIN FETCH e.immediateBoss JOIN FETCH e.workCenter WHERE e.idssff = :idssff")
    Optional<EmployeeEntity> findOneByIdssff(@Param("idssff") Long idssff);
    @Query("SELECT e FROM EmployeeEntity e JOIN FETCH e.immediateBoss JOIN FETCH e.workCenter WHERE e.isActive = true")
    Page<EmployeeEntity> findAllActiveEmployees(Pageable pageable);
}
