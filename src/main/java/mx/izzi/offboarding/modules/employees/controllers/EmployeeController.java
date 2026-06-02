package mx.izzi.offboarding.modules.employees.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.employees.domain.dtos.DetailEmployeeDto;
import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.employees.services.EmployeeService;
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
@RequestMapping("/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(value = "/{idssff}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("RRHH")
    public ResponseEntity<OBResponse<DetailEmployeeDto>> getById(@PathVariable Long idssff) {
        return this.employeeService.findOneBy(idssff)
                .map(EmployeeMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("RRHH")
    public ResponseEntity<OBResponse<Page<DetailEmployeeDto>>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<DetailEmployeeDto> employees = this.employeeService.findAll(page, size)
                .map(EmployeeMapper::from);
        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, employees, HttpStatus.OK);
    }
}
