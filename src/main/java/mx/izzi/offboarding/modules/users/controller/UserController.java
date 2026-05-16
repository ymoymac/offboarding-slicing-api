package mx.izzi.offboarding.modules.users.controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.UpdateUserDto;
import mx.izzi.offboarding.modules.users.domain.models.UserMapper;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.enums.OBResponseCodes;
import mx.izzi.offboarding.shared.mappers.ResponseMapper;
import mx.izzi.offboarding.shared.models.OBResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{idssff}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<OBResponse<DetailUserDto>> getById(@PathVariable String idssff) {
        return this.userService.findOneBy(Long.parseLong(idssff))
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_USER, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping("/profile/{idssff}")
    @RolesAllowed({"IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<DetailUserDto>> getUserProfileById(@PathVariable String idssff) {
        return this.userService.findOneProfileBy(Long.parseLong(idssff))
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_USER, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @PostMapping
    @RolesAllowed({"ADMIN", "IMMEDIATE_BOSS"})
    public ResponseEntity<OBResponse<DetailUserDto>> create(@RequestBody @Valid CreateUserDto createUserDto) {
        return this.userService.create(createUserDto)
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.USER_CREATED, userDto, HttpStatus.CREATED))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<OBResponse<Page<DetailUserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DetailUserDto> users = this.userService.findAll(page, size)
                .map(UserMapper::from);

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_USER, users, HttpStatus.OK);
    }

    @PatchMapping("/{idssff}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<OBResponse<DetailUserDto>> update(
            @PathVariable String idssff,
            @RequestBody @Valid UpdateUserDto updateUserDto
    ) {
        return this.userService.update(Long.parseLong(idssff), updateUserDto)
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_USER, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @DeleteMapping("/{idssff}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<OBResponse<DetailUserDto>> delete(@PathVariable String idssff) {
        return this.userService.delete(Long.parseLong(idssff))
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_USER, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }
}

