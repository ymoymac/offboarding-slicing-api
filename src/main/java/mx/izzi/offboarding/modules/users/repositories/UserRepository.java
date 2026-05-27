package mx.izzi.offboarding.modules.users.repositories;

import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("""
        SELECT DISTINCT u
        FROM UserEntity u
        JOIN FETCH u.roleUserUnion ruu
            JOIN FETCH ruu.role
        JOIN FETCH u.workCenter
        WHERE u.idssff = :idssff
    """)
    Optional<UserEntity> findOneByIdssff(@Param("idssff") Long idssff);

    @Query("""
        SELECT DISTINCT u
        FROM UserEntity u
        JOIN FETCH u.roleUserUnion r
            JOIN FETCH r.role
        JOIN FETCH u.workCenter
        WHERE u.isActive = true
    """
    )
    Page<UserEntity> findAllActiveUsersBy(Pageable pageable);

    boolean existsById(@NonNull Long idssff);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
