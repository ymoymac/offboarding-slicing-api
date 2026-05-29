package mx.izzi.offboarding.modules.auth.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDto {

    @NotNull
    @NotBlank
    @NotEmpty
    @Email
    @JsonProperty
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    @JsonProperty
    private String password;
}