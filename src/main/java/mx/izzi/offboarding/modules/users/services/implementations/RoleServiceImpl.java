package mx.izzi.offboarding.modules.users.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.users.domain.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.domain.models.Role;
import mx.izzi.offboarding.modules.users.repositories.RoleRepository;
import mx.izzi.offboarding.modules.users.services.RoleService;
import mx.izzi.offboarding.shared.exceptions.ResourceNotAvailableException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final String PATH = "";
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findOneBy(Long id) {
        Optional<Role> role = this.roleRepository.findById(id)
                .map(RoleEntity::toDomain);

        if (role.isEmpty()) {
            throw new ResourceNotFoundException(PATH);
        }

        if (role.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH);
        }

        return role;
    }
}
