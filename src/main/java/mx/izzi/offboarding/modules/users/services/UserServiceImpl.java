package mx.izzi.offboarding.modules.users.services;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.users.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.entities.UserEntity;
import mx.izzi.offboarding.modules.users.models.UserMapper;
import mx.izzi.offboarding.modules.users.models.Role;
import mx.izzi.offboarding.modules.users.models.User;
import mx.izzi.offboarding.modules.users.repositories.RoleRepository;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.shared.exceptions.ResourceAlreadyExistsException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<User> findOneBy(Long idssff) {
        return Optional.of(this.userRepository.findOneByIdssff(idssff).toDomain());
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

        Optional<Role> role = this.roleRepository.findById(2L).map(RoleEntity::toDomain);

        if (role.isEmpty() || role.get().getIsActive().equals(false)) {
            throw new ResourceNotFoundException("Role not found");
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
                .build();

        return Optional.of(this.userRepository.saveAndFlush(UserMapper.toEntity(user)).toDomain());
    }
}
