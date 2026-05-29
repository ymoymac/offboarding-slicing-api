package mx.izzi.offboarding.modules.users.services;

import jakarta.persistence.EntityManager;
import mx.izzi.offboarding.Factory;
import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.domain.entities.RoleUserUnionEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.repositories.RoleUserUnionRepository;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.modules.users.services.implementations.UserServiceImpl;
import mx.izzi.offboarding.modules.workcenter.services.WorkCenterService;
import mx.izzi.offboarding.shared.enums.Roles;
import mx.izzi.offboarding.shared.exceptions.EmailAlreadyExistsException;
import mx.izzi.offboarding.shared.exceptions.ResourceAlreadyExistsException;
import mx.izzi.offboarding.shared.exceptions.ServerException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateUserTest {

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    EntityManager entityManager;

    @Mock
    UserRepository userRepository;
    @Mock
    RoleUserUnionRepository roleUserUnionRepository;

    @Mock
    WorkCenterService workCenterService;
    @Mock
    RoleService roleService;

    @InjectMocks
    UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        Factory.mockSecurityContext(Factory.userDetails(Factory.user(Roles.ADMIN)));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @Order(1)
    @DisplayName("It should return one user after create")
    void create() {

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Mockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(this.userRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(false);

        RoleEntity roleEntity = Factory.roleEntity(Factory.immediateRole());
        Mockito.when(this.roleService.findOneBy(Mockito.anyLong()))
                .thenReturn(Optional.of(Factory.immediateRole()));

        Mockito.when(this.workCenterService.findOneBy(Mockito.anyLong()))
                .thenReturn(Optional.of(Factory.workCenter()));

        Mockito.when(this.passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("$0932j$9u2j0");

        UserEntity savedEntity = Factory.userEntity(Factory.user(Roles.IMMEDIATE_BOSS));
        Mockito.when(this.userRepository.saveAndFlush(Mockito.any(UserEntity.class)))
                .thenReturn(savedEntity);

        RoleUserUnionEntity roleUserUnionEntity = Factory.roleUserUnionEntity(
                Factory.roleUserUnion(Factory.immediateRole(), savedEntity.toDomain()),
                roleEntity,
                savedEntity
        );
        Mockito.when(this.roleUserUnionRepository.saveAndFlush(Mockito.any(RoleUserUnionEntity.class)))
                .thenReturn(roleUserUnionEntity);

        Mockito.doNothing().when(this.entityManager).clear();

        savedEntity.setRoleUserUnion(Set.of(roleUserUnionEntity));

        Mockito.when(this.userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(savedEntity));

        CreateUserDto createUserDto = Factory.createUserDto();
        Optional<User> created = this.userService.create(createUserDto);

        assertThat(created.isPresent()).isTrue();
    }

    @Test
    @Order(2)
    @DisplayName("It should throw a ResourceAlreadyExistsException error")
    void create_throwResourceAlreadyExists() {
        UserEntity founded = Factory.userEntity(Factory.user(Roles.IMMEDIATE_BOSS));
        CreateUserDto createUserDto = Factory.createUserDto();
        createUserDto.setIdssff(999999L);

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.of(founded));

        assertThatThrownBy(() -> this.userService.create(createUserDto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("/api/v1/users");
    }

    @Test
    @Order(3)
    @DisplayName("It should throw a ResourceAlreadyExistsException error caused by email")
    void create_throwResourceAlreadyExists_byEmail() {
        CreateUserDto createUserDto = Factory.createUserDto();

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Mockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> this.userService.create(createUserDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("/api/v1/users");
    }

    @Test
    @Order(4)
    @DisplayName("It should throw a ResourceAlreadyExistsException error caused by username")
    void create_throwResourceAlreadyExists_byUsername() {
        CreateUserDto createUserDto = Factory.createUserDto();
        createUserDto.setIdssff(999999L);

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Mockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(this.userRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> this.userService.create(createUserDto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("/api/v1/users");
    }

    @Test
    @Order(5)
    @DisplayName("It should throw a ServerException error caused by role")
    void create_throwServerException_byRole() {
        CreateUserDto createUserDto = Factory.createUserDto();
        createUserDto.setIdssff(999999L);

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Mockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(this.userRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(this.roleService.findOneBy(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.userService.create(createUserDto))
                .isInstanceOf(ServerException.class)
                .hasMessageContaining("/api/v1/users");
    }

    @Test
    @Order(6)
    @DisplayName("It should throw a ServerException error caused by work center")
    void create_throwServerException_byWorkCenter() {
        CreateUserDto createUserDto = Factory.createUserDto();
        createUserDto.setIdssff(999999L);

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Mockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(this.userRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(this.roleService.findOneBy(Mockito.anyLong()))
                .thenReturn(Optional.of(Factory.immediateRole()));

        Mockito.when(this.workCenterService.findOneBy(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.userService.create(createUserDto))
                .isInstanceOf(ServerException.class)
                .hasMessageContaining("/api/v1/users");
    }

    @Test
    @Order(7)
    @DisplayName("It should throw a ResourceNotFoundException error caused by work center")
    void create_throwServerException_errorCreateUser() {
        CreateUserDto createUserDto = Factory.createUserDto();
        createUserDto.setIdssff(999999L);

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Mockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(this.userRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(this.roleService.findOneBy(Mockito.anyLong()))
                .thenReturn(Optional.of(Factory.immediateRole()));

        Mockito.when(this.workCenterService.findOneBy(Mockito.anyLong()))
                .thenReturn(Optional.of(Factory.workCenter()));

        Mockito.when(this.passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("$0932j$9u2j0");

        Mockito.when(this.userRepository.saveAndFlush(Mockito.any(UserEntity.class)))
                .thenThrow(new DataAccessResourceFailureException("DB error connection"));

        assertThatThrownBy(() -> this.userService.create(createUserDto))
                .isInstanceOf(DataAccessResourceFailureException.class)
                .hasMessageContaining("DB error connection");
    }
}
