package mx.izzi.offboarding.modules.auth.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthUserDto {
    @JsonProperty
    private DetailUserDto user;

    @JsonProperty
    private String token;

}
