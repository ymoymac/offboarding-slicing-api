package mx.izzi.offboarding.modules.terminations.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.terminations.domain.entities.TerminationReasonEntity;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationReason;
import mx.izzi.offboarding.modules.terminations.repositories.TerminationReasonRepository;
import mx.izzi.offboarding.modules.terminations.services.TerminationReasonService;
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
public class TerminationReasonServiceImpl implements TerminationReasonService {

    private final String PATH = "/api/v1/terminations/reasons";
    private final TerminationReasonRepository terminationReasonRepository;

    @Override
    public Optional<TerminationReason> findOneBy(Long terminationReasonId) {
        Optional<TerminationReason> terminationReason = this.terminationReasonRepository
                .findById(terminationReasonId)
                .map(TerminationReasonEntity::toDomain);

        if (terminationReason.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + terminationReasonId);
        }

        if (terminationReason.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH + "/" + terminationReasonId);
        }

        return terminationReason;
    }

    @Override
    public Page<TerminationReason> findAll(int page, int size) {
        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }
        Pageable pageable = PageRequest.of(page, size);

        return this.terminationReasonRepository
                .findAll(pageable)
                .map(TerminationReasonEntity::toDomain);
    }
}
