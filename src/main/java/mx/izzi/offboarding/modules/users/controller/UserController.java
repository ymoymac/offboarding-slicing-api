package mx.izzi.offboarding.modules.users.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.users.dtos.CreateUserDto;
import mx.izzi.offboarding.modules.users.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.models.UserMapper;
import mx.izzi.offboarding.modules.users.services.UserService;
import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.enums.OBResponseCodes;
import mx.izzi.offboarding.shared.mappers.ResponseMapper;
import mx.izzi.offboarding.shared.models.OBResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{idssff}")
    public ResponseEntity<OBResponse<DetailUserDto>> getById(@PathVariable String idssff) {
        return this.userService.findOneBy(Long.parseLong(idssff))
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.GET_USER, userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @PostMapping
    public ResponseEntity<OBResponse<DetailUserDto>> create(@RequestBody @Valid CreateUserDto createUserDto) {
        return this.userService.create(createUserDto)
                .map(UserMapper::from)
                .map(userDto -> ResponseMapper.map(OBResponseCodes.USER_CREATED, userDto, HttpStatus.CREATED))
                .orElseGet(() -> ResponseMapper.toError(OBErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}

