package mx.izzi.offboarding.modules.users.services;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.users.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.models.RoleEntity;
import mx.izzi.offboarding.modules.users.models.UserEntity;
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
    public Optional<UserEntity> findOneBy(Long idssff) {
        return Optional.of(this.userRepository.findOneByIdssff(idssff));
    }

    @Transactional
    @Override
    public Optional<UserEntity> create(CreateUserDto createUserDto) {
        UserEntity userEntityFound = this.userRepository.findOneByIdssff(createUserDto.getIdssff());


        if (userEntityFound != null) {
            throw new ResourceAlreadyExistsException("User already exists");
        }

        if (this.userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (this.userRepository.existsByNickname(createUserDto.getEmail().split("@")[0])) {
            throw new ResourceAlreadyExistsException("Nickname already exists");
        }

        Optional<RoleEntity> role = this.roleRepository.findById(2L);

        if (role.isEmpty() || role.get().getIsActive().equals(false)) {
            throw new ResourceNotFoundException("Role not found");
        }

        LocalDateTime now = LocalDateTime.now();

        UserEntity userEntity = UserEntity.builder()
                .idssff(createUserDto.getIdssff())
                .name(createUserDto.getName())
                .firstSurname(createUserDto.getFirstSurname())
                .secondSurname(createUserDto.getSecondSurname())
                .email(createUserDto.getEmail())
                .nickname(createUserDto.getEmail().split("@")[0])
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("System")
                .updatedBy("System")
                .role(role.get())
                .build();

        UserEntity userSaved = this.userRepository.saveAndFlush(userEntity);

        return Optional.of(userSaved);
    }
}
