package mx.izzi.offboarding.modules.users.repositories;

import mx.izzi.offboarding.modules.users.domain.entities.UserRoleUnionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleUnionRepository extends JpaRepository<UserRoleUnionEntity, Integer> {
}
