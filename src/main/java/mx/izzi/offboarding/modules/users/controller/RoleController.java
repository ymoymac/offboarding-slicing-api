package mx.izzi.offboarding.modules.users.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.users.domain.dtos.AssignRoleDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailRoleDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailRoleUserUnionDto;
import mx.izzi.offboarding.modules.users.domain.mappers.RoleMapper;
import mx.izzi.offboarding.modules.users.domain.mappers.RoleUserUnionMapper;
import mx.izzi.offboarding.modules.users.services.RoleService;
import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.enums.OBResponseCodes;
import mx.izzi.offboarding.shared.mappers.ResponseMapper;
import mx.izzi.offboarding.shared.models.OBResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
@RolesAllowed("ADMIN")
public class RoleController {

    private final RoleService roleService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBResponse<DetailRoleDto>> getRole(@PathVariable long id) {
        return this.roleService.findOneBy(id)
                .map(RoleMapper::fromToDto)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBResponse<Page<DetailRoleDto>>> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DetailRoleDto> users = this.roleService.findAll(page, size)
                .map(RoleMapper::fromToDto);

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, users, HttpStatus.OK);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBResponse<DetailRoleUserUnionDto>> assignRole(@RequestBody AssignRoleDto assignRoleDto) {
        return this.roleService.assignRole(assignRoleDto)
                .map(RoleUserUnionMapper::fromToDto)
                .map(dto -> ResponseMapper.map(OBResponseCodes.ROLE_ASSIGNED, dto, HttpStatus.CREATED))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }
}
