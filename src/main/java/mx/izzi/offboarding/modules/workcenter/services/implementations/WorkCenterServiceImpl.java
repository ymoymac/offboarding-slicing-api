package mx.izzi.offboarding.modules.workcenter.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;
import mx.izzi.offboarding.modules.workcenter.repositories.WorkCenterRepository;
import mx.izzi.offboarding.modules.workcenter.services.WorkCenterService;
import mx.izzi.offboarding.shared.exceptions.ResourceNotAvailableException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotFoundException;
import mx.izzi.offboarding.shared.exceptions.ValueNotValidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkCenterServiceImpl implements WorkCenterService {

    private final String PATH = "/api/v1/workcenters";

    private final WorkCenterRepository workCenterRepository;
    @Override
    public Optional<WorkCenter> findOneBy(Long id) {
        Optional<WorkCenter> workCenter = this.workCenterRepository
                .findById(id)
                .map(WorkCenterEntity::toDomain);

        if (workCenter.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + id);
        }

        if (workCenter.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH + "/" + id);
        }

        return workCenter;
    }

    @Override
    public Page<WorkCenter> findAllBy(int page, int size) {
        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.workCenterRepository
                .findAllActiveWorkCentersBy(pageable)
                .map(WorkCenterEntity::toDomain);
    }
}
