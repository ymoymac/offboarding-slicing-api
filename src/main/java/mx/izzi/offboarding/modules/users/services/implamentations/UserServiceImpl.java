package mx.izzi.offboarding.modules.users.services.implamentations;

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
import mx.izzi.offboarding.modules.workcenter.domain.WorkCenterEntity;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;
import mx.izzi.offboarding.modules.workcenter.repositories.WorkCenterRepository;
import mx.izzi.offboarding.shared.exceptions.ResourceAccessDeniedException;
import mx.izzi.offboarding.shared.exceptions.ResourceAlreadyExistsException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotAvailableException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotFoundException;
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

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final WorkCenterRepository workCenterRepository;

    @Override
    public Optional<User> findOneBy(Long idssff) {
        return Optional.of(this.userRepository.findOneByIdssff(idssff).toDomain());
    }

    @Override
    public Optional<User> findOneProfileBy(Long idssff) {
        User user = this.userRepository.findOneByIdssff(idssff).toDomain();

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user);

        boolean isAdmin = authenticatedUser.isAdmin();

        boolean isSameUser = userDetails.getUsername().equals(idssff.toString());

        if  (!isAdmin && !isSameUser) {
            throw new ResourceAccessDeniedException("Access denied");
        }

        return Optional.of(user);
    }

    @Transactional
    @Override
    public Optional<User> create(CreateUserDto createUserDto) {
        UserEntity userFound = this.userRepository.findOneByIdssff(createUserDto.getIdssff());

        if (userFound != null) {
            throw new ResourceAlreadyExistsException("User already exists");
        }

        if (this.userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (this.userRepository.existsByUsername(createUserDto.getEmail().split("@")[0])) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        Optional<Role> role = this.roleRepository
                .findById(2L)
                .map(RoleEntity::toDomain);

        if (role.isEmpty() || role.get().getIsActive().equals(false)) {
            throw new ResourceNotFoundException("Role not found");
        }

        Optional<WorkCenter> workCenter = this.workCenterRepository
                .findById(createUserDto.getWorkCenterId())
                .map(WorkCenterEntity::toDomain);

        if (workCenter.isEmpty() || workCenter.get().getIsActive().equals(false)) {
            throw new ResourceNotFoundException("Work center not found");
        }

        LocalDateTime now = LocalDateTime.now();

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
                .createdBy("System")
                .updatedBy("System")
                .role(role.get())
                .workCenter(workCenter.get())
                .build();

        return Optional.of(this.userRepository.saveAndFlush(UserMapper.toEntity(user)).toDomain());
    }

    @Override
    public Page<User> findAll(int page, int size) {
        if (!List.of(5, 10, 20).contains(size)) {
            throw new IllegalArgumentException("Size must be between 5 and 20");
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.userRepository.findAllActiveUserBy(pageable).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> update(Long idssff, UpdateUserDto updateUserDto) {

        WorkCenter workCenter = null;

        UserEntity userFound = this.userRepository.findOneByIdssff(idssff);

        if (userFound == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (userFound.getIsActive().equals(false)) {
            throw new ResourceNotAvailableException("User not active");
        }

        String password = updateUserDto.getPassword() != null
                ? passwordEncoder.encode(updateUserDto.getPassword())
                : userFound.getPassword();

        if (updateUserDto.getWorkCenterId() != null) {
            workCenter = this.workCenterRepository
                    .findById(updateUserDto.getWorkCenterId())
                    .map(WorkCenterEntity::toDomain)
                    .orElse(null);

            if (workCenter == null) {
                throw new ResourceNotFoundException("Work center not found");
            }
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = User.builder()
                .idssff(userFound.getIdssff())
                .name(userFound.getName())
                .firstSurname(userFound.getFirstSurname())
                .secondSurname(userFound.getSecondSurname())
                .email(userFound.getEmail())
                .username(userFound.getUsername())
                .password(password)
                .isActive(true)
                .createdAt(userFound.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .createdBy(userFound.getCreatedBy())
                .updatedBy(userDetails.getUsername())
                .role(userFound.getRole().toDomain())
                .workCenter(workCenter)
                .build();


        return Optional.of(this.userRepository.saveAndFlush(UserMapper.toEntity(user)).toDomain());
    }

    @Override
    public Optional<User> delete(Long idssff) {
        UserEntity userFound = this.userRepository.findOneByIdssff(idssff);

        if (userFound == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (userFound.getIsActive().equals(false)) {
            throw new ResourceNotAvailableException("User not active");
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        userFound.setIsActive(false);
        userFound.setUpdatedBy(userDetails.getUsername());
        return Optional.of(this.userRepository.saveAndFlush(userFound).toDomain());

    }
}
