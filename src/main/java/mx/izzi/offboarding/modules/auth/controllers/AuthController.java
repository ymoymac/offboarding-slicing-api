package mx.izzi.offboarding.modules.auth.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.auth.domain.dtos.AuthUserDto;
import mx.izzi.offboarding.modules.auth.domain.dtos.LoginDto;
import mx.izzi.offboarding.modules.auth.domain.dtos.SignUpDto;
import mx.izzi.offboarding.modules.auth.domain.models.AuthMapper;
import mx.izzi.offboarding.modules.auth.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthUserDto> login(@RequestBody @Valid LoginDto loginDto) {
        return this.authService.login(loginDto)
                .map(AuthMapper::from)
                .map(userDto -> ResponseEntity.ok().body(userDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

    @PostMapping(value = "/signup",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthUserDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        return this.authService.signUp(signUpDto)
                .map(AuthMapper::from)
                .map(userDto -> ResponseEntity.ok().body(userDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}

