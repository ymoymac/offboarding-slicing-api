package mx.izzi.offboarding.modules.users.services;

import mx.izzi.offboarding.Factory;
import mx.izzi.offboarding.modules.auth.domain.models.AuthUtils;
import mx.izzi.offboarding.modules.users.domain.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.domain.entities.RoleUserUnionEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.modules.users.services.implementations.UserServiceImpl;
import mx.izzi.offboarding.shared.enums.Roles;
import mx.izzi.offboarding.shared.exceptions.ResourceAccessDeniedException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotAvailableException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FindOneProfileByTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        Factory.mockSecurityContext(Factory.userDetails(Factory.user(Roles.IMMEDIATE_BOSS)));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @Order(1)
    @DisplayName("It should return the information of the user who logged in.")
    void findOneProfileBy() {
        RoleEntity roleEntity = Factory.roleEntity(Factory.immediateRole());
        UserEntity userEntity = Factory.userEntity(Factory.user(Roles.IMMEDIATE_BOSS));
        RoleUserUnionEntity roleUserUnionEntity = Factory.roleUserUnionEntity(
                Factory.roleUserUnion(Factory.immediateRole(), userEntity.toDomain()),
                roleEntity,
                userEntity
        );

        userEntity.setRoleUserUnion(Set.of(roleUserUnionEntity));

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.of(userEntity));

        Optional<User> user = this.userService.findOneProfileBy(Mockito.anyLong());

        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUserId()).isEqualTo(userEntity.getUserId());
        assertThat(AuthUtils.isAdmin(user.get())).isEqualTo(false);
    }

    @Test
    @Order(2)
    @DisplayName("It should throw an exception because the logged-in user is requesting another user's information.")
    void findOneProfileBy_throwAccessDenied() {

        RoleEntity roleEntity = Factory.roleEntity(Factory.immediateRole());
        UserEntity userEntity = Factory.userEntity(Factory.user(Roles.IMMEDIATE_BOSS));
        RoleUserUnionEntity roleUserUnionEntity = Factory.roleUserUnionEntity(
                Factory.roleUserUnion(Factory.immediateRole(), userEntity.toDomain()),
                roleEntity,
                userEntity
        );

        userEntity.setRoleUserUnion(Set.of(roleUserUnionEntity));

        User rrhhUser = Factory.user(Roles.RRHH);
        SecurityContextHolder.clearContext();
        Factory.mockSecurityContext(Factory.userDetails(rrhhUser));

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.of(userEntity));

        assertThatThrownBy(() -> this.userService.findOneProfileBy(99L))
                .isInstanceOf(ResourceAccessDeniedException.class)
                .hasMessageContaining("/api/v1/users/profile/99");
    }

    @Test
    @Order(3)
    @DisplayName("It should throw a ResourceNotFoundException because the user does not exist")
    void findOneProfileBy_throwNotFound() {

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.userService.findOneProfileBy(Mockito.anyLong()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("/api/v1/users/profile/0");
    }

    @Test
    @Order(4)
    @DisplayName("It should throw a ResourceNotAvailableException because the user exists but is not active")
    void findOneProfileBy_throwNotAvailable() {

        RoleEntity roleEntity = Factory.roleEntity(Factory.immediateRole());
        UserEntity userEntity = Factory.userEntity(Factory.user(Roles.IMMEDIATE_BOSS));
        RoleUserUnionEntity roleUserUnionEntity = Factory.roleUserUnionEntity(
                Factory.roleUserUnion(Factory.immediateRole(), userEntity.toDomain()),
                roleEntity,
                userEntity
        );

        userEntity.setRoleUserUnion(Set.of(roleUserUnionEntity));
        userEntity.setIsActive(false);

        Mockito.when(this.userRepository.findOneByIdssff(Mockito.anyLong()))
                .thenReturn(Optional.of(userEntity));

        assertThatThrownBy(() -> this.userService.findOneProfileBy(Mockito.anyLong()))
                .isInstanceOf(ResourceNotAvailableException.class)
                .hasMessageContaining("/api/v1/users/profile/0");
    }
}
