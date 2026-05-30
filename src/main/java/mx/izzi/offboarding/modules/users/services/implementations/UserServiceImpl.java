package mx.izzi.offboarding.modules.users.services.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.izzi.offboarding.modules.auth.domain.models.AuthUtils;
import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.UpdateUserDto;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.mappers.RoleMapper;
import mx.izzi.offboarding.modules.users.domain.mappers.RoleUserUnionMapper;
import mx.izzi.offboarding.modules.users.domain.mappers.UserMapper;
import mx.izzi.offboarding.modules.users.domain.models.*;
import mx.izzi.offboarding.modules.users.repositories.RoleUserUnionRepository;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.modules.users.services.RoleService;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;
import mx.izzi.offboarding.modules.workcenter.services.WorkCenterService;
import mx.izzi.offboarding.shared.exceptions.*;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String PATH = "/api/v1/users";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final WorkCenterService workCenterService;
    private final RoleUserUnionRepository roleUserUnionRepository;

    @PersistenceContext
    private final EntityManager entityManager;

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
    public Optional<User> findOneByEmail(String email) {
        Optional<User> user = this.userRepository
                .findOneByEmail(email)
                .map(UserEntity::toDomain);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException(PATH);
        }

        if (user.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH);
        }

        return user;
    }

    @Override
    public Optional<User> findOneProfileBy(Long idssff) {

        Optional<User> user = this.userRepository
                .findOneByIdssff(idssff)
                .map(UserEntity::toDomain);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/profile/" + idssff);
        }

        if (user.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH + "/profile/" + idssff);
        }

        if  (!AuthUtils.isSameUser(user.get())) {
            throw new ResourceAccessDeniedException(PATH + "/profile/" + idssff);
        }

        return user;
    }

    @Transactional
    @Override
    public Optional<User> create(CreateUserDto createUserDto) {
        Optional<User> userFound = this.userRepository
                .findOneByIdssff(createUserDto.getIdssff())
                .map(UserEntity::toDomain);

        if (userFound.isPresent()) {
            throw new ResourceAlreadyExistsException(PATH);
        }

        if (this.userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new EmailAlreadyExistsException(PATH);
        }

        if (this.userRepository.existsByUsername(createUserDto.getEmail().split("@")[0])) {
            throw new ResourceAlreadyExistsException(PATH);
        }

        Optional<Role> role = this.roleService.findOneBy(createUserDto.getRoleId());

        if  (role.isEmpty()) {
            throw new ServerException(PATH);
        }

        Optional<WorkCenter> workCenter = this.workCenterService.findOneBy(createUserDto.getWorkCenterId());

        if (workCenter.isEmpty()) {
            throw new ServerException(PATH);
        }

        LocalDateTime now = LocalDateTime.now();

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String createdBy = principal instanceof UserDetails userDetails
                ? userDetails.getUsername()
                : "System";

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
                .createdBy(createdBy)
                .updatedBy(createdBy)
                .roles(List.of(role.get()))
                .workCenter(workCenter.get())
                .build();

        User saved = this.userRepository.saveAndFlush(UserMapper.toEntity(user)).toDomain();

        RoleUserUnion union = RoleUserUnion.builder()
                .role(role.get())
                .user(saved)
                .assignmentDate(now)
                .assignedBy(createdBy)
                .isActive(true)
                .updatedAt(now)
                .updatedBy(createdBy)
                .createdBy(createdBy)
                .build();

        this.roleUserUnionRepository.saveAndFlush(
                RoleUserUnionMapper.toEntity(
                        union,
                        RoleMapper.toEntity(role.get()),
                        UserMapper.toEntity(saved)
                )
        );

        this.entityManager.clear();

        return this.userRepository
                .findById(saved.getUserId())
                .map(UserEntity::toDomain);
    }

    @Override
    public Page<User> findAll(int page, int size) {
        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.userRepository
                .findAllUsers(pageable)
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
                .roles(userFound.get().getRoles())
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
