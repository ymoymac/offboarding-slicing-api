package mx.izzi.offboarding.modules.users.services.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.auth.domain.models.AuthUtils;
import mx.izzi.offboarding.modules.users.domain.dtos.AssignRoleDto;
import mx.izzi.offboarding.modules.users.domain.entities.RoleEntity;
import mx.izzi.offboarding.modules.users.domain.entities.RoleUserUnionEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.mappers.RoleUserUnionMapper;
import mx.izzi.offboarding.modules.users.domain.models.Role;
import mx.izzi.offboarding.modules.users.domain.models.RoleUserUnion;
import mx.izzi.offboarding.modules.users.repositories.RoleRepository;
import mx.izzi.offboarding.modules.users.repositories.RoleUserUnionRepository;
import mx.izzi.offboarding.modules.users.repositories.UserRepository;
import mx.izzi.offboarding.modules.users.services.RoleService;
import mx.izzi.offboarding.shared.exceptions.ResourceNotAvailableException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotFoundException;
import mx.izzi.offboarding.shared.exceptions.ResourceRoleAlreadyAssignedException;
import mx.izzi.offboarding.shared.exceptions.ValueNotValidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final String PATH = "/api/v1/roles";

    @PersistenceContext
    private final EntityManager entityManager;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleUserUnionRepository roleUserUnionRepository;

    @Override
    public Optional<Role> findOneBy(Long id) {
        Optional<Role> role = this.roleRepository.findById(id)
                .map(RoleEntity::toDomain);

        if (role.isEmpty()) {
            throw new ResourceNotFoundException(PATH + "/" + id);
        }

        if (role.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH);
        }

        return role;
    }

    @Override
    public Page<Role> findAll(int page, int size) {
        if (!List.of(5, 10, 20).contains(size)) {
            throw new ValueNotValidException(PATH + "?page=" + page + "&size=" + size);
        }

        Pageable pageable = PageRequest.of(page, size);
        return roleRepository.findAll(pageable)
                .map(RoleEntity::toDomain);
    }

    @Transactional
    @Override
    public Optional<RoleUserUnion> assignRole(AssignRoleDto assignRoleDto) {


        Optional<UserEntity> userEntity = this.userRepository.findOneByIdssff(assignRoleDto.getUserIdssff());

        if (userEntity.isEmpty()) {
            throw new ResourceNotFoundException("/api/v1/users/" + assignRoleDto.getUserIdssff());
        }

        if (userEntity.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH);
        }

        Optional<RoleEntity> roleEntity = this.roleRepository.findById(assignRoleDto.getRoleId());

        if (roleEntity.isEmpty()) {
            throw new ResourceNotFoundException(PATH + assignRoleDto.getRoleId());
        }

        if (roleEntity.get().getIsActive().equals(false)) {
            throw new ResourceNotAvailableException(PATH + assignRoleDto.getRoleId());
        }

        if (this.roleUserUnionRepository.existsByRoleAndUser(userEntity.get().getUserId(), roleEntity.get().getRoleId())) {
            throw new ResourceRoleAlreadyAssignedException(PATH);
        }

        UserDetails userDetails = AuthUtils.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();
        RoleUserUnion union = RoleUserUnion.builder()
                .assignmentDate(now)
                .assignedBy(userDetails.getUsername())
                .isActive(true)
                .updatedAt(now)
                .createdBy(userDetails.getUsername())
                .updatedBy(userDetails.getUsername())
                .build();

        RoleUserUnionEntity saved = this.roleUserUnionRepository.saveAndFlush(
                RoleUserUnionMapper.toEntity(union, roleEntity.get(), userEntity.get())
        );

        this.entityManager.clear();

        return this.roleUserUnionRepository.findById(saved.getId())
                .map(RoleUserUnionEntity::allToDomain);
    }
}
