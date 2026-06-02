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
import mx.izzi.offboarding.modules.users.domain.dtos.*;
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

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final TerminationService terminationService;

    @GetMapping(value = "/profile/{idssff}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<DetailUserWithRoleDto>> getProfileById(@PathVariable Long idssff) {
        return this.userService.findOneProfileBy(idssff)
                .map(UserMapper::fromToDto)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<DetailUserWithRoleDto>> getByIdEmail(@RequestBody @Valid SearchByDto dto) {
        return this.userService.findOneByEmail(dto.getEmail())
                .map(UserMapper::fromToDto)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping("/{idssff}/employees")
    @RolesAllowed({"IMMEDIATE_BOSS"})
    public ResponseEntity<OBResponse<List<DetailEmployeeDto>>> getAllEmployees(@PathVariable Long idssff) {
        List<DetailEmployeeDto> employees = this.employeeService.findAllEmployeesByImmediateBoss(idssff)
                .stream()
                .map(EmployeeMapper::from)
                .toList();

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, employees, HttpStatus.OK);
    }

    @GetMapping("/{idssff}/employees/{employeeIdssff}")
    @RolesAllowed({"IMMEDIATE_BOSS", "RRHH"})
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
        Page<DetailAccessBlockRequestDto> terminations = this.terminationService
                .findAllTerminationsByImmediateBoss(idssff, page, size)
                .map(TerminationMapper::from);

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, terminations, HttpStatus.OK);
    }
}

