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

    @JsonProperty
    private Long idssff;

    @NotNull
    @NotBlank
    @NotEmpty
    @JsonProperty
    private String password;
}