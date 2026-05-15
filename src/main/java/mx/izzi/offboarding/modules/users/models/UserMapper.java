package mx.izzi.offboarding.modules.users.models;

import mx.izzi.offboarding.modules.users.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.entities.UserEntity;

public class UserMapper {
    public static DetailUserDto from(User user) {
        return DetailUserDto.builder()
                .idssff(user.getIdssff())
                .name(user.getName())
                .firstSurname(user.getFirstSurname())
                .secondSurname(user.getSecondSurname())
                .email(user.getEmail())
                .nickname(user.getUsername())
                .isActive(user.getIsActive())
                .build();
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .idssff(user.getIdssff())
                .name(user.getName())
                .firstSurname(user.getFirstSurname())
                .secondSurname(user.getSecondSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .isActive(user.getIsActive())
                .role(RoleMapper.toEntity(user.getRole()))
                .build();
    }
}
