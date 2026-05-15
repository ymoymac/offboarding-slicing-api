package mx.izzi.offboarding.modules.users.mappers;

import mx.izzi.offboarding.modules.users.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.models.UserEntity;

public class UserMapper {
    public static DetailUserDto from(UserEntity user) {
        return DetailUserDto.builder()
                .idssff(user.getIdssff())
                .name(user.getName())
                .firstSurname(user.getFirstSurname())
                .secondSurname(user.getSecondSurname())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .isActive(user.getIsActive())
                .build();
    }
}
