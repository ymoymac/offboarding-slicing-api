package mx.izzi.offboarding.modules.auth.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.auth.domain.models.AuthenticatedUser;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        LOG.info("[INFO]: Executing UserDetailsService for user '{}'", email);

        Optional<User> user = this.userRepository
                .findOneByEmail(email)
                .map(UserEntity::toDomain);

        LOG.info("[INFO]: User '{}' found", user);

        if  (user.isEmpty()) {
            throw new BadCredentialsException("/api/v1/auth/login");
        }

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user.get());

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
}

