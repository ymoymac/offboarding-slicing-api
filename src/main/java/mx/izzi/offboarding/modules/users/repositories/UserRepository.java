package mx.izzi.offboarding.modules.users.repositories;

import mx.izzi.offboarding.modules.users.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.role WHERE u.idssff = :idssff")
    UserEntity findOneByIdssff(Long idssff);
    boolean existsById(@NonNull Long idssff);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
