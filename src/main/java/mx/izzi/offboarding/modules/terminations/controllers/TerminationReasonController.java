package mx.izzi.offboarding.modules.terminations.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailTerminationReasonDto;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationMapper;
import mx.izzi.offboarding.modules.terminations.services.TerminationReasonService;
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
@RequestMapping("/v1/terminations/reasons")
@RequiredArgsConstructor
public class TerminationReasonController {
    private final TerminationReasonService terminationReasonService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN", "IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<DetailTerminationReasonDto>> getById(@PathVariable long id) {
        return this.terminationReasonService.findOneBy(id)
                .map(TerminationMapper::from)
                .map(dto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, dto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN", "IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<Page<DetailTerminationReasonDto>>> getAllReasons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DetailTerminationReasonDto> types = this.terminationReasonService.findAll(page, size)
                .map(TerminationMapper::from);

        return ResponseMapper.map(OBResponseCodes.LIST_ACTIVE_RESOURCES, types, HttpStatus.OK);
    }
}
