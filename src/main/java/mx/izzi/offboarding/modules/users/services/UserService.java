package mx.izzi.offboarding.modules.users.services;

import mx.izzi.offboarding.modules.users.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.models.UserEntity;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findOneBy(Long idssff);
    Optional<UserEntity> create(CreateUserDto user);
}
