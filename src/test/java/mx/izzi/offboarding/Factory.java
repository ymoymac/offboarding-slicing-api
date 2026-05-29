package mx.izzi.offboarding;

import mx.izzi.offboarding.modules.auth.domain.models.AuthenticatedUser;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.users.domain.models.Role;
import mx.izzi.offboarding.modules.users.domain.models.RoleUserUnion;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;
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

    public static WorkCenter workCenter() {
        return WorkCenter.builder()
                .region("Centro")
                .locality("Ciudad de México")
                .company("Izzi")
                .workCenter("Santa Fe")
                .department("Desarrollo TT")
                .dependents(10)
                .isActive(true)
                .createdAt(LocalDateTime.now().minusMonths(3))
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
    }

    public static RoleUserUnion roleUserUnion(Role role) {
        return RoleUserUnion.builder()
                .id(1L)
                .role(role)
                .user(user(immediateRole()))
                .assignmentDate(LocalDateTime.now().minusMonths(3))
                .assignedBy("System")
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
    }

    public static User user(Role ...roles) {
        return User.builder()
                .userId(999999999L)
                .idssff(123456L)
                .name("Enrique Manuel")
                .firstSurname("García")
                .secondSurname("García")
                .username("egarcia")
                .email("egarcia@izzi.mx")
                .password("pass123")
                .roles(List.of(roles))
                .workCenter(workCenter())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().minusMonths(3))
                .createdBy("System")
                .updatedBy("System")
                .build();
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
                .immediateBoos(user(immediateRole()))
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
