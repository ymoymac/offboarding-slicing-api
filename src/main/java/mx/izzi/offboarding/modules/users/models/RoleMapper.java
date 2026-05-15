package mx.izzi.offboarding.modules.users.models;

import mx.izzi.offboarding.modules.users.entities.RoleEntity;

public class RoleMapper {
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
