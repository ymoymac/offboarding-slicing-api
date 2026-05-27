package mx.izzi.offboarding.modules.users.repositories;

import mx.izzi.offboarding.modules.users.domain.entities.RoleUserUnionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleUserUnionRepository extends JpaRepository<RoleUserUnionEntity, Integer> {
}
