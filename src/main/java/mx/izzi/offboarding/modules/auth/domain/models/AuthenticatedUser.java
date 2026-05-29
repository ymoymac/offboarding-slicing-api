package mx.izzi.offboarding.modules.auth.domain.models;

import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.shared.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record AuthenticatedUser(User user) implements UserDetails {

    public boolean isAdmin() {
        if (user.getRoles().size() > 1) {
            return false;
        }
        return Roles.ADMIN.getName().equals(user.getRoles().getFirst().getName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.getIsActive();
    }

    @Override
    public boolean isEnabled() {
        return this.user.getIsActive();
    }
}
