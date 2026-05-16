package mx.izzi.offboarding.modules.users.repositories;

import mx.izzi.offboarding.modules.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.role JOIN FETCH u.workCenter WHERE u.idssff = :idssff")
    UserEntity findOneByIdssff(@Param("idssff") Long idssff);
    boolean existsById(@NonNull Long idssff);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
