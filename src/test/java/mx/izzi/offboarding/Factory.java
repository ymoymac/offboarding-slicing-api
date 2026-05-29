package mx.izzi.offboarding;

import mx.izzi.offboarding.modules.auth.domain.models.AuthenticatedUser;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.domain.entities.RoleUserUnionEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.mappers.RoleMapper;
import mx.izzi.offboarding.modules.users.domain.mappers.RoleUserUnionMapper;
import mx.izzi.offboarding.modules.users.domain.mappers.UserMapper;
import mx.izzi.offboarding.modules.users.domain.models.Role;
import mx.izzi.offboarding.modules.users.domain.models.RoleUserUnion;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenterMapper;
import mx.izzi.offboarding.shared.enums.Roles;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

public final class Factory {

    public static Role adminRole() {
        return Role.builder()
                .roleId(1L)
                .name(Roles.ADMIN.name())
                .description(Roles.ADMIN.getAlias())
                .isActive(true)
                .createdAt(LocalDateTime.now().minusMonths(3))
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
    }

    public static Role immediateRole() {
        return Role.builder()
                .roleId(2L)
                .name(Roles.IMMEDIATE_BOSS.name())
                .description(Roles.IMMEDIATE_BOSS.getAlias())
                .isActive(true)
                .createdAt(LocalDateTime.now().minusMonths(3))
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
    }

    public static Role rrhhRole() {
        return Role.builder()
                .roleId(3L)
                .name(Roles.RRHH.name())
                .description(Roles.RRHH.getAlias())
                .isActive(true)
                .createdAt(LocalDateTime.now().minusMonths(3))
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
    }

    public static RoleEntity roleEntity(Role role) {
        return RoleMapper.toEntity(role);
    }

    public static WorkCenter workCenter() {
        return WorkCenter.builder()
                .id(1L)
                .regionId("21")
                .region("Centro")
                .localityId("99")
                .locality("Ciudad de México")
                .companyId("001")
                .company("Izzi")
                .workCenterId("002")
                .workCenter("Santa Fe")
                .departmentId("99")
                .department("Desarrollo TT")
                .dependents(10)
                .isActive(true)
                .createdAt(LocalDateTime.now().minusMonths(3))
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
    }

    public static WorkCenterEntity workCenterEntity(WorkCenter workCenter) {
        return WorkCenterMapper.toEntity(workCenter);
    }

    public static RoleUserUnion roleUserUnion(Role role, User user) {
        return RoleUserUnion.builder()
                .id(1L)
                .role(role)
                .user(user)
                .assignmentDate(LocalDateTime.now().minusMonths(3))
                .assignedBy("System")
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
    }

    public static RoleUserUnionEntity roleUserUnionEntity(RoleUserUnion roleUserUnion, RoleEntity roleEntity, UserEntity userEntity) {
        return RoleUserUnionMapper.toEntity(roleUserUnion, roleEntity, userEntity);
    }

    public static User user(Roles role) {
        return switch (role) {
            case Roles.ADMIN -> User.builder()
                    .userId(1L)
                    .idssff(1L)
                    .name("ADMIN Enrique Manuel")
                    .firstSurname("García")
                    .secondSurname("García")
                    .username("egarcia")
                    .email("egarcia.admin@izzi.mx")
                    .password("admin2026")
                    .roles(List.of(adminRole()))
                    .workCenter(workCenter())
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now().minusMonths(3))
                    .createdBy("System")
                    .updatedBy("System")
                    .build();
            case Roles.IMMEDIATE_BOSS -> User.builder()
                    .userId(999999999L)
                    .idssff(123456L)
                    .name("IMMEDIATE_BOSS John James")
                    .firstSurname("Doe")
                    .secondSurname("Smith")
                    .email("jdoe@email.com")
                    .username("jdoe")
                    .password("password")
                    .roles(List.of(immediateRole()))
                    .workCenter(workCenter())
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now().minusMonths(3))
                    .createdBy("System")
                    .updatedBy("System")
                    .build();
            default -> User.builder()
                    .userId(10011001L)
                    .idssff(444L)
                    .name("RRHH Ana Sofia")
                    .firstSurname("De la cuadra")
                    .secondSurname("Rojas")
                    .email("adelacuadra@email.com")
                    .username("adelacuadra")
                    .password("rrhh2026")
                    .roles(List.of(rrhhRole()))
                    .workCenter(workCenter())
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now().minusMonths(3))
                    .createdBy("System")
                    .updatedBy("System")
                    .build();
        };
    }

    public static UserEntity userEntity(User user) {
        return UserMapper.toEntity(user);
    }

    public static UserDetails userDetails(User user) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(authenticatedUser.user().getEmail())
                .password(authenticatedUser.user().getPassword())
                .authorities(
                        authenticatedUser.user().getRoles()
                                .stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .toList()
                )
                .accountExpired(!authenticatedUser.isAccountNonExpired())
                .accountLocked(!authenticatedUser.isAccountNonLocked())
                .credentialsExpired(!authenticatedUser.isCredentialsNonExpired())
                .disabled(!authenticatedUser.user().getIsActive())
                .build();
    }

    public static CreateUserDto createUserDto() {
        return CreateUserDto.builder()
                .idssff(0L)
                .name("IMMEDIATE_BOSS John James")
                .firstSurname("Doe")
                .secondSurname("Smith")
                .email("jdoe@email.com")
                .password("password")
                .workCenterId(1L)
                .roleId(2L)
                .build();
    }

    public static Employee employee() {
        return Employee.builder()
                .employeeId(100000001L)
                .idssff(98765L)
                .name("Juan Manuel")
                .firstSurname("Riquelme")
                .secondSurname("Sanchez")
                .email("jriquelme@izzi.mx")
                .positionId("99")
                .position("Desarrollador Jr TT")
                .jobPositionId("67")
                .jobPosition("9908")
                .immediateBoos(user(Roles.IMMEDIATE_BOSS))
                .workCenter(workCenter())
                .isActive(true)
                .createdAt(LocalDateTime.now().minusMonths(3))
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
    }

    public static void mockSecurityContext(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }
}
