package mx.izzi.offboarding.modules.auth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.auth.dtos.AuthUserDto;
import mx.izzi.offboarding.modules.auth.dtos.LoginDto;
import mx.izzi.offboarding.modules.auth.dtos.SignUpDto;
import mx.izzi.offboarding.modules.auth.mappers.AuthMapper;
import mx.izzi.offboarding.modules.auth.services.AuthService;
import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.enums.OBResponseCodes;
import mx.izzi.offboarding.shared.mappers.ResponseMapper;
import mx.izzi.offboarding.shared.models.OBResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        return this.authService.login(loginDto)
                .map(AuthMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.USER_LOGIN, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        return this.authService.signUp(signUpDto)
                .map(AuthMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.USER_CREATED, userDto, HttpStatus.OK))
                .get();
    }

    @GetMapping("/debug")
    public String debug(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}

