package mx.izzi.offboarding.modules.terminations.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.terminations.domain.entities.TerminationTypeEntity;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationType;
import mx.izzi.offboarding.modules.terminations.repositories.TerminationTypeRepository;
import mx.izzi.offboarding.modules.terminations.services.TerminationTypeService;
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
public class TerminationTypeServiceImpl implements TerminationTypeService {

    private final String PATH = "/api/v1/terminations/types";
    private final TerminationTypeRepository terminationTypeRepository;

    @Override
    public Optional<TerminationType> findOneBy(Long terminationTypeId) {
        Optional<TerminationType> terminationType = this.terminationTypeRepository
                .findById(terminationTypeId)
                .map(TerminationTypeEntity::toDomain);

        if (terminationType.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + terminationTypeId);
        }

        if (terminationType.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH + "/" + terminationTypeId);
        }

        return terminationType;
    }

    @Override
    public Page<TerminationType> findAll(int page, int size) {
        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }
        Pageable pageable = PageRequest.of(page, size);

        return this.terminationTypeRepository
                .findAll(pageable)
                .map(TerminationTypeEntity::toDomain);
    }
}
