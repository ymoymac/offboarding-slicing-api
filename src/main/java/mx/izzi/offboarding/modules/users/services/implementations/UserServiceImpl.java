package mx.izzi.offboarding.modules.users.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.auth.domain.models.AuthenticatedUser;
import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.UpdateUserDto;
import mx.izzi.offboarding.modules.users.domain.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.UserMapper;
import mx.izzi.offboarding.modules.users.domain.models.Role;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.repositories.RoleRepository;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;
import mx.izzi.offboarding.modules.workcenter.services.WorkCenterService;
import mx.izzi.offboarding.shared.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    private final String PATH = "/api/v1/users";

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final WorkCenterService workCenterService;

    @Override
    public Optional<User> findOneBy(Long idssff) {
        Optional<User> user = this.userRepository
                .findOneByIdssff(idssff)
                .map(UserEntity::toDomain);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + idssff);
        }

        if (user.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH + "/" + idssff);
        }

        return user;
    }

    @Override
    public Optional<User> findOneProfileBy(Long idssff) {
        Optional<User> user = this.userRepository.findOneByIdssff(idssff)
                .map(UserEntity::toDomain);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" +idssff);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user.get());

        boolean isAdmin = authenticatedUser.isAdmin();

        boolean isSameUser = userDetails.getUsername().equals(idssff.toString());

        if  (!isAdmin && !isSameUser) {
            throw new ResourceAccessDeniedException(PATH + "/profile/" +idssff);
        }

        return user;
    }

    @Transactional
    @Override
    public Optional<User> create(CreateUserDto createUserDto) {
        Optional<UserEntity> userFound = this.userRepository.findOneByIdssff(createUserDto.getIdssff());

        if (userFound.isPresent()) {
            throw new ResourceAlreadyExistsException(PATH);
        }

        if (this.userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new ResourceAlreadyExistsException(PATH);
        }

        if (this.userRepository.existsByUsername(createUserDto.getEmail().split("@")[0])) {
            throw new ResourceAlreadyExistsException(PATH);
        }

        Optional<Role> role = this.roleRepository
                .findById(2L)
                .map(RoleEntity::toDomain);

        if (role.isEmpty() || role.get().getIsActive().equals(false)) {
            throw new ResourceNotFoundException(PATH);
        }

        Optional<WorkCenter> workCenter = this.workCenterService.findOneBy(createUserDto.getWorkCenterId());

        if (workCenter.isEmpty() || workCenter.get().getIsActive().equals(false)) {
            throw new ResourceNotFoundException(PATH);
        }

        LocalDateTime now = LocalDateTime.now();

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = User.builder()
                .idssff(createUserDto.getIdssff())
                .name(createUserDto.getName())
                .firstSurname(createUserDto.getFirstSurname())
                .secondSurname(createUserDto.getSecondSurname())
                .email(createUserDto.getEmail())
                .username(createUserDto.getEmail().split("@")[0])
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy(userDetails.getUsername())
                .updatedBy(userDetails.getUsername())
                .role(role.get())
                .workCenter(workCenter.get())
                .build();

        return Optional.of(this.userRepository.saveAndFlush(UserMapper.toEntity(user)).toDomain());
    }

    @Override
    public Page<User> findAll(int page, int size) {
        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.userRepository
                .findAllActiveUsersBy(pageable)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> update(Long idssff, UpdateUserDto updateUserDto) {

        WorkCenter workCenter = null;

        Optional<User> userFound = this.findOneBy(idssff);

        if  (userFound.isEmpty()) {
            throw new ServerException(PATH);
        }

        String password = updateUserDto.getPassword() != null
                ? passwordEncoder.encode(updateUserDto.getPassword())
                : userFound.get().getPassword();

        if (updateUserDto.getWorkCenterId() != null) {
            Optional<WorkCenter> workCenterFound = this.workCenterService.findOneBy(updateUserDto.getWorkCenterId());

            if (workCenterFound.isEmpty()) {
                throw new ResourceNotFoundException(PATH + "/" + idssff);
            }

            workCenter = workCenterFound.get();
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = User.builder()
                .idssff(userFound.get().getIdssff())
                .name(userFound.get().getName())
                .firstSurname(userFound.get().getFirstSurname())
                .secondSurname(userFound.get().getSecondSurname())
                .email(userFound.get().getEmail())
                .username(userFound.get().getUsername())
                .password(password)
                .isActive(true)
                .createdAt(userFound.get().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .createdBy(userFound.get().getCreatedBy())
                .updatedBy(userDetails.getUsername())
                .role(userFound.get().getRole())
                .workCenter(workCenter)
                .build();

        return Optional.of(this.userRepository.saveAndFlush(UserMapper.toEntity(user)).toDomain());
    }

    @Override
    public Optional<User> delete(Long idssff) {
        Optional<User> userFound = this.findOneBy(idssff);

        if  (userFound.isEmpty()) {
            throw new ServerException(PATH);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        userFound.get().setIsActive(false);
        userFound.get().setUpdatedBy(userDetails.getUsername());
        return Optional.of(this.userRepository.saveAndFlush(UserMapper.toEntity(userFound.get())).toDomain());

    }
}
