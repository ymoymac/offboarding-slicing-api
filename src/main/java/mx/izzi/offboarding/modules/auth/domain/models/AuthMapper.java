package mx.izzi.offboarding.modules.auth.domain.models;

import mx.izzi.offboarding.modules.auth.domain.dtos.AuthUserDto;
import mx.izzi.offboarding.modules.auth.domain.dtos.SignUpDto;
import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.models.UserMapper;

public class AuthMapper {

    public static AuthUserDto from(AuthUser authUser) {
        return AuthUserDto.builder()
                .user(UserMapper.from(authUser.getUser()))
                .token(authUser.getToken())
                .build();
    }

    public static CreateUserDto from(SignUpDto signUpDto) {
        return CreateUserDto.builder()
                .idssff(signUpDto.getIdssff())
                .name(signUpDto.getName())
                .firstSurname(signUpDto.getFirstSurname())
                .secondSurname(signUpDto.getSecondSurname())
                .password(signUpDto.getPassword())
                .email(signUpDto.getEmail())
                .workCenterId(signUpDto.getWorkCenterId())
                .build();
    }
}
