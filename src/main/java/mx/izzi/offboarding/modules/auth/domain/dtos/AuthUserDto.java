package mx.izzi.offboarding.modules.auth.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserWithRoleDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthUserDto {
    @JsonProperty
    private DetailUserWithRoleDto user;

    @JsonProperty
    private String token;

}
