package mx.izzi.offboarding.modules.workcenter.repositories;

import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkCenterRepository extends JpaRepository<WorkCenterEntity, Long> {
    @Query("SELECT wc FROM WorkCenterEntity wc WHERE wc.isActive = true")
    Page<WorkCenterEntity> findAllActiveWorkCentersBy(Pageable pageable);
}
