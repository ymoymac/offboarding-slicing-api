package mx.izzi.offboarding.modules.users.domain.mappers;

import mx.izzi.offboarding.modules.users.domain.dtos.DetailRoleDto;
import mx.izzi.offboarding.modules.users.domain.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.domain.models.Role;

public class RoleMapper {
    public static DetailRoleDto from(Role role) {
        return DetailRoleDto.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    public static RoleEntity toEntity(Role role) {
        return RoleEntity.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .description(role.getDescription())
                .isActive(role.getIsActive())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .createdBy(role.getCreatedBy())
                .updatedBy(role.getUpdatedBy())
                .build();
    }
}
