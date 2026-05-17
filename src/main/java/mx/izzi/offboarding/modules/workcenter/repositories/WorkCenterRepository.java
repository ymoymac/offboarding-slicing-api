package mx.izzi.offboarding.modules.workcenter.repositories;

import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkCenterRepository extends JpaRepository<WorkCenterEntity, Long> {
}
