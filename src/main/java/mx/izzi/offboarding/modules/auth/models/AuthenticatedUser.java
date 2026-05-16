package mx.izzi.offboarding.modules.auth.models;

import mx.izzi.offboarding.modules.users.models.User;
import mx.izzi.offboarding.shared.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record AuthenticatedUser(User user) implements UserDetails {

    public boolean isAdmin() {
        return Roles.ADMIN.getName().equals(user.getRole().getName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.user.getRole().getName()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getIdssff().toString();
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
