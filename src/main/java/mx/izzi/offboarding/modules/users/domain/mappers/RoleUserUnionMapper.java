package mx.izzi.offboarding.modules.users.domain.mappers;

import mx.izzi.offboarding.modules.users.domain.dtos.DetailRoleUserUnionDto;
import mx.izzi.offboarding.modules.users.domain.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.domain.entities.RoleUserUnionEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.models.RoleUserUnion;

public class RoleUserUnionMapper {

    public static RoleUserUnionEntity toEntity(RoleUserUnion roleUserUnion, RoleEntity roleEntity, UserEntity userEntity) {
        return RoleUserUnionEntity.builder()
                .role(roleEntity)
                .user(userEntity)
                .assignmentDate(roleUserUnion.getAssignmentDate())
                .assignedBy(roleUserUnion.getAssignedBy())
                .isActive(roleUserUnion.getIsActive())
                .updatedAt(roleUserUnion.getUpdatedAt())
                .createdBy(roleUserUnion.getCreatedBy())
                .updatedBy(roleUserUnion.getUpdatedBy())
                .build();
    }

    public static DetailRoleUserUnionDto fromToDto(RoleUserUnion roleUserUnion) {
        return DetailRoleUserUnionDto.builder()
                .role(RoleMapper.fromToDto(roleUserUnion.getRole()))
                .user(UserMapper.fromToDto(roleUserUnion.getUser()))
                .assignmentDate(roleUserUnion.getAssignmentDate())
                .assignedBy(roleUserUnion.getAssignedBy())
                .build();
    }
}
