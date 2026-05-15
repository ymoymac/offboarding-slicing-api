package mx.izzi.offboarding.modules.users.repositories;

import mx.izzi.offboarding.modules.users.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
