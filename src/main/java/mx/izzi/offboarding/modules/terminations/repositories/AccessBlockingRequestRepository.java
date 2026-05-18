package mx.izzi.offboarding.modules.terminations.repositories;

import mx.izzi.offboarding.modules.terminations.domain.entities.AccessBlockingRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessBlockingRequestRepository extends JpaRepository<AccessBlockingRequestEntity, Long> {
    Optional<AccessBlockingRequestEntity> findByFolio(String folio);
}
