package mx.izzi.offboarding.modules.workcenter.services;

import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface WorkCenterService {
    Optional<WorkCenter> findOneBy(Long id);
    Page<WorkCenter> findAllBy(int page, int size);
}
