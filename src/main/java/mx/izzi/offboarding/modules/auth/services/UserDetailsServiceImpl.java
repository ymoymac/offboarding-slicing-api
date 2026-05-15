package mx.izzi.offboarding.modules.auth.services;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.auth.models.SecurityUser;
import mx.izzi.offboarding.modules.users.entities.UserEntity;
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
        SecurityUser securityUser = new SecurityUser(user.toDomain());

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(securityUser.user().getIdssff().toString())
                .password(securityUser.user().getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(securityUser.user().getRole().getName())))
                .accountExpired(!securityUser.isAccountNonExpired())
                .accountLocked(!securityUser.isAccountNonLocked())
                .credentialsExpired(!securityUser.isCredentialsNonExpired())
                .disabled(!securityUser.user().getIsActive())
                .build();
    }
}

