package mx.izzi.offboarding.modules.users.repositories;

import mx.izzi.offboarding.modules.users.domain.entities.RoleUserUnionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleUserUnionRepository extends JpaRepository<RoleUserUnionEntity, Long> {
    @Query("""
        SELECT COUNT(ruu) > 0
        FROM RoleUserUnionEntity ruu
        WHERE ruu.user.userId = :userId
        AND ruu.role.roleId = :roleId
    """)
    boolean existsByRoleAndUser(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
