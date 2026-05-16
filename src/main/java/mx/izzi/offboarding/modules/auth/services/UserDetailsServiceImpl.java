package mx.izzi.offboarding.modules.auth.services;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.auth.models.AuthenticatedUser;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String idssff) {

        LOG.info("[INFO]: Executing UserDetailsService for user '{}'", idssff);

        UserEntity user = userRepository.findOneByIdssff(Long.parseLong(idssff));

        if  (user == null) {
            throw new BadCredentialsException(idssff);
        }
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user.toDomain());

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(authenticatedUser.user().getIdssff().toString())
                .password(authenticatedUser.user().getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(authenticatedUser.user().getRole().getName())))
                .accountExpired(!authenticatedUser.isAccountNonExpired())
                .accountLocked(!authenticatedUser.isAccountNonLocked())
                .credentialsExpired(!authenticatedUser.isCredentialsNonExpired())
                .disabled(!authenticatedUser.user().getIsActive())
                .build();
    }
}

