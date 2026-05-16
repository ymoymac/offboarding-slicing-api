package mx.izzi.offboarding.modules.users.services;

import mx.izzi.offboarding.modules.users.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.models.User;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface UserService {
    Optional<User> findOneBy(Long idssff);
    Optional<User> findOneProfileBy(Long idssff);
    Optional<User> create(CreateUserDto user);
}
