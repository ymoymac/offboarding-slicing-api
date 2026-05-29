package mx.izzi.offboarding.modules.auth.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MicrosoftUserDto {
    @JsonProperty
    private String token;

    @JsonProperty
    private String email;
}
