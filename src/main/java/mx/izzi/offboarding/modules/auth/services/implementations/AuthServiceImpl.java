package mx.izzi.offboarding.modules.auth.services.implementations;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.auth.domain.dtos.LoginDto;
import mx.izzi.offboarding.modules.auth.domain.dtos.SignUpDto;
import mx.izzi.offboarding.modules.auth.domain.models.AuthMapper;
import mx.izzi.offboarding.modules.auth.domain.models.AuthUser;
import mx.izzi.offboarding.modules.auth.domain.models.AuthenticatedUser;
import mx.izzi.offboarding.modules.auth.services.AuthService;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.security.services.JwtService;
import mx.izzi.offboarding.shared.exceptions.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final String PATH = "/api/v1/auth";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userJpaRepository;
    private final UserService userService;

    @Override
    public Optional<AuthUser> login(LoginDto loginDto) {
        LOG.info("[INFO]: Login {}", loginDto);

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getIdssff(), loginDto.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = this.jwtService.generateToken(userDetails);

        User user = this.userJpaRepository
                .findOneByIdssff(loginDto.getIdssff())
                .map(UserEntity::toDomain)
                .orElse(null);

        return Optional.of(
                AuthUser.builder()
                        .user(user)
                        .token(token)
                        .build()
        );
    }

    @Override
    public Optional<AuthUser> signUp(SignUpDto signUpDto) {
        LOG.info("[INFO]: SignUp {}", signUpDto);

        Optional<User> user = this.userService.create(AuthMapper.from(signUpDto));

        if (user.isEmpty()) {
            throw new ServerException(PATH + "/" + "signup");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(user.get().getIdssff().toString())
                .password(user.get().getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.get().getRole().getName())))
                .accountLocked(!user.get().getIsActive())
                .accountLocked(!user.get().getIsActive())
                .credentialsExpired(!user.get().getIsActive())
                .disabled(!user.get().getIsActive())
                .build();

        LOG.info("[INFO]: User {}", userDetails);

        String token = this.jwtService.generateToken(userDetails);

        LOG.info("[INFO]: Token {}", token);

        return Optional.of(
                AuthUser.builder()
                        .user(user.get())
                        .token(token)
                        .build()
        );
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AuthenticatedUser) authentication.getPrincipal();
    }
}
