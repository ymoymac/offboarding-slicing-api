package mx.izzi.offboarding.modules.users.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserWithRoleDto;
import mx.izzi.offboarding.modules.users.domain.dtos.UpdateUserDto;
import mx.izzi.offboarding.modules.users.domain.mappers.UserMapper;
import mx.izzi.offboarding.modules.users.services.UserService;
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
@RequestMapping("/v1")
@RolesAllowed("ADMIN")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping(value = "/{idssff}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBResponse<DetailUserWithRoleDto>> getById(@PathVariable Long idssff) {
        return this.userService.findOneBy(idssff)
                .map(UserMapper::fromToDto)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBResponse<Page<DetailUserWithRoleDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DetailUserWithRoleDto> users = this.userService.findAll(page, size)
                .map(UserMapper::fromToDto);

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, users, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBResponse<DetailUserWithRoleDto>> create(@RequestBody @Valid CreateUserDto createUserDto) {
        return this.userService.create(createUserDto)
                .map(UserMapper::fromToDto)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.CREATED, userDto, HttpStatus.CREATED))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PatchMapping(value = "/{idssff}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBResponse<DetailUserDto>> update(
            @PathVariable String idssff,
            @RequestBody @Valid UpdateUserDto updateUserDto
    ) {
        return this.userService.update(Long.parseLong(idssff), updateUserDto)
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @DeleteMapping(value = "/{idssff}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBResponse<DetailUserDto>> delete(@PathVariable String idssff) {
        return this.userService.delete(Long.parseLong(idssff))
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

}
