package mx.izzi.offboarding.modules.terminations.repositories;

import mx.izzi.offboarding.modules.terminations.domain.entities.AccessBlockRequestEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccessBlockRequestRepository extends JpaRepository<AccessBlockRequestEntity, Long> {
    Optional<AccessBlockRequestEntity> findByFolio(String folio);

    @Query("""
        SELECT t FROM AccessBlockRequestEntity t
        JOIN FETCH t.applicantUser
        JOIN FETCH t.immediateBoss
        JOIN FETCH t.employee
        JOIN FETCH t.terminationType
        JOIN FETCH t.terminationReason
        WHERE t.applicantUser = :user
    """)
    Page<AccessBlockRequestEntity> findByUser(Pageable pageable, UserEntity user);
}
