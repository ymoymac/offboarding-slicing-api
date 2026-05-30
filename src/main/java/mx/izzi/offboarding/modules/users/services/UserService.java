package mx.izzi.offboarding.modules.users.services;

import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.UpdateUserDto;
import mx.izzi.offboarding.modules.users.domain.models.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findOneBy(Long idssff);
    Optional<User> findOneByEmail(String email);
    Optional<User> findOneProfileBy(Long idssff);
    Optional<User> create(CreateUserDto user);
    Page<User> findAll(int page, int size);
    Optional<User> update(Long idssff, UpdateUserDto updateUserDto);
    Optional<User> delete(Long idssff);
}
