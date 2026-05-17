package mx.izzi.offboarding.modules.users.domain.models;

import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserWithRoleDto;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenterMapper;

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

    public static DetailUserWithRoleDto toDto(User user) {
        return DetailUserWithRoleDto.builder()
                .idssff(user.getIdssff())
                .name(user.getName())
                .firstSurname(user.getFirstSurname())
                .secondSurname(user.getSecondSurname())
                .email(user.getEmail())
                .nickname(user.getUsername())
                .isActive(user.getIsActive())
                .role(RoleMapper.from(user.getRole()))
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
                .workCenter(WorkCenterMapper.toEntity(user.getWorkCenter()))
                .build();
    }
}
