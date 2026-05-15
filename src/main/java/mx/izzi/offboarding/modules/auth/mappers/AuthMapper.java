package mx.izzi.offboarding.modules.auth.mappers;

import mx.izzi.offboarding.modules.auth.dtos.AuthUserDto;
import mx.izzi.offboarding.modules.auth.dtos.SignUpDto;
import mx.izzi.offboarding.modules.auth.models.AuthUser;
import mx.izzi.offboarding.modules.users.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.mappers.UserMapper;

public class AuthMapper {

    public static AuthUserDto from(AuthUser authUser) {
        DetailUserDto user = UserMapper.from(authUser.getUser());
        return AuthUserDto.builder()
                .user(user)
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
                .build();
    }
}
