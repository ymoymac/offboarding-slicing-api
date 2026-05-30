package mx.izzi.offboarding.modules.users.services;

import mx.izzi.offboarding.modules.users.domain.dtos.AssignRoleDto;
import mx.izzi.offboarding.modules.users.domain.models.Role;
import mx.izzi.offboarding.modules.users.domain.models.RoleUserUnion;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findOneBy(Long id);
    Page<Role> findAll(int page, int size);
    Optional<RoleUserUnion> assignRole(AssignRoleDto assignRoleDto);
}
