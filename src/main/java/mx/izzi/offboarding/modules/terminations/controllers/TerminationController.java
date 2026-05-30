package mx.izzi.offboarding.modules.terminations.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.terminations.domain.dtos.CreateAccessBlockRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailAccessBlockRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationMapper;
import mx.izzi.offboarding.modules.terminations.services.TerminationService;
import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.enums.OBResponseCodes;
import mx.izzi.offboarding.shared.mappers.ResponseMapper;
import mx.izzi.offboarding.shared.models.OBResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/terminations")
@RequiredArgsConstructor
public class TerminationController {

    private final TerminationService terminationService;

    @GetMapping(value = "/{folio}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<DetailAccessBlockRequestDto>> getByFolio(@PathVariable String folio) {
        return this.terminationService.findOneBy(folio)
                .map(TerminationMapper::from)
                .map(dto -> ResponseMapper.map(OBResponseCodes.GET_RESOURCE, dto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<DetailAccessBlockRequestDto>> create(@RequestBody @Valid CreateAccessBlockRequestDto createAccessBlockRequestDto) {
        return this.terminationService.create(createAccessBlockRequestDto)
                .map(TerminationMapper::from)
                .map(dto -> ResponseMapper.map(OBResponseCodes.CREATED, dto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }
}
