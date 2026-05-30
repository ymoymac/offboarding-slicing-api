package mx.izzi.offboarding.modules.users.services;

import mx.izzi.offboarding.modules.users.domain.models.Role;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findOneBy(Long id);
    Page<Role> findAll(int page, int size);
}
