package mx.izzi.offboarding.modules.terminations.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.terminations.domain.dtos.CreateAccessBlockingRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailAccessBlockingRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationMapper;
import mx.izzi.offboarding.modules.terminations.services.TerminationService;
import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.enums.OBResponseCodes;
import mx.izzi.offboarding.shared.mappers.ResponseMapper;
import mx.izzi.offboarding.shared.models.OBResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/terminations")
@RequiredArgsConstructor
public class TerminationController {

    private final TerminationService terminationService;

    @PostMapping
    @RolesAllowed({"ADMIN", "IMMEDIATE_BOSS", "RRHH"})
    public ResponseEntity<OBResponse<DetailAccessBlockingRequestDto>> create(@RequestBody @Valid CreateAccessBlockingRequestDto createAccessBlockingRequestDto) {
        return this.terminationService.create(createAccessBlockingRequestDto)
                .map(TerminationMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.CREATED, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }
}
