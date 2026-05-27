package mx.izzi.offboarding.modules.users.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.employees.domain.dtos.DetailEmployeeDto;
import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.employees.services.EmployeeService;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailAccessBlockRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationMapper;
import mx.izzi.offboarding.modules.terminations.services.TerminationService;
import mx.izzi.offboarding.modules.users.domain.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserWithRoleDto;
import mx.izzi.offboarding.modules.users.domain.dtos.UpdateUserDto;
import mx.izzi.offboarding.modules.users.domain.models.UserMapper;
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

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final TerminationService terminationService;

    @GetMapping(value = "/{idssff}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<OBResponse<DetailUserWithRoleDto>> getById(@PathVariable String idssff) {
        return this.userService.findOneBy(Long.parseLong(idssff))
                .map(UserMapper::fromToDto)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping(value = "/profile/{idssff}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<DetailUserDto>> getUserProfileById(@PathVariable String idssff) {
        return this.userService.findOneProfileBy(Long.parseLong(idssff))
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<OBResponse<DetailUserDto>> create(@RequestBody @Valid CreateUserDto createUserDto) {
        return this.userService.create(createUserDto)
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.CREATED, userDto, HttpStatus.CREATED))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<OBResponse<Page<DetailUserWithRoleDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DetailUserWithRoleDto> users = this.userService.findAll(page, size)
                .map(UserMapper::fromToDto);

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, users, HttpStatus.OK);
    }

    @PatchMapping(value = "/{idssff}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
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
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<OBResponse<DetailUserDto>> delete(@PathVariable String idssff) {
        return this.userService.delete(Long.parseLong(idssff))
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping("/{idssff}/employees")
    @RolesAllowed({"IMMEDIATE_BOSS"})
    public ResponseEntity<OBResponse<List<DetailEmployeeDto>>> getAllEmployees(@PathVariable String idssff) {
        List<DetailEmployeeDto> employees = this.employeeService.findAllEmployeesByImmediateBoss(Long.parseLong(idssff))
                .stream()
                .map(EmployeeMapper::from)
                .toList();

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, employees, HttpStatus.OK);
    }

    @GetMapping("/{idssff}/employees/{employeeIdssff}")
    @RolesAllowed({"IMMEDIATE_BOSS"})
    public ResponseEntity<OBResponse<DetailEmployeeDto>> getEmployeeById(
            @PathVariable Long idssff,
            @PathVariable Long employeeIdssff
    ) {
        return this.employeeService.findOneEmployeeByImmediateBoss(idssff, employeeIdssff)
                .map(EmployeeMapper::from)
                .map(employeeDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, employeeDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping("/{idssff}/terminations")
    @RolesAllowed({"IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<Page<DetailAccessBlockRequestDto>>> getAllTerminationsByUserId(
            @PathVariable Long idssff,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DetailAccessBlockRequestDto> terminations = this.terminationService.findAllTerminationsByUserId(idssff, page, size)
                .map(TerminationMapper::from);

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, terminations, HttpStatus.OK);
    }
}

