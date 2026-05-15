package mx.izzi.offboarding.modules.auth.services;

import mx.izzi.offboarding.modules.auth.dtos.LoginDto;
import mx.izzi.offboarding.modules.auth.dtos.SignUpDto;
import mx.izzi.offboarding.modules.auth.models.AuthUser;

import java.util.Optional;

public interface AuthService {
    Optional<AuthUser> login(LoginDto loginDto);
    Optional<AuthUser> signUp(SignUpDto signUpDto);
    boolean validateToken(String token);
}
