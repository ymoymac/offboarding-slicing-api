package mx.izzi.offboarding.modules.users.domain.mappers;

import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserWithRoleDto;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenterMapper;

import java.util.Collections;

public class UserMapper {
    public static DetailUserDto from(User user) {
        return DetailUserDto.builder()
                .idssff(user.getIdssff())
                .name(user.getName())
                .firstSurname(user.getFirstSurname())
                .secondSurname(user.getSecondSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .workCenter(WorkCenterMapper.from(user.getWorkCenter()))
                .build();
    }

    public static DetailUserWithRoleDto fromToDto(User user) {
        return DetailUserWithRoleDto.builder()
                .idssff(user.getIdssff())
                .name(user.getName())
                .firstSurname(user.getFirstSurname())
                .secondSurname(user.getSecondSurname())
                .email(user.getEmail())
                .nickname(user.getUsername())
                .isActive(user.getIsActive())
                .roles(user.getRoles().stream().map(RoleMapper::fromToDto).toList())
                .workCenter(WorkCenterMapper.from(user.getWorkCenter()))
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
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .roleUserUnion(Collections.emptySet())
                .workCenter(WorkCenterMapper.toEntity(user.getWorkCenter()))
                .build();
    }
}
