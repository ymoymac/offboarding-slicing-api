package mx.izzi.offboarding.modules.users.domain.dtos;

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
public class CreateUserDto {

    @JsonProperty
    private Long idssff;

    @NotBlank(message = "Name is mandatory")
    @NotEmpty(message = "Name can not be empty")
    @NotNull(message = "Name cannot be null")
    @JsonProperty
    private String name;

    @NotBlank(message = "Name is mandatory")
    @NotEmpty(message = "Name can not be empty")
    @NotNull(message = "Name cannot be null")
    @JsonProperty
    private String firstSurname;

    @NotBlank(message = "Name is mandatory")
    @NotEmpty(message = "Name can not be empty")
    @NotNull(message = "Name cannot be null")
    @JsonProperty
    private String secondSurname;

    @NotBlank(message = "Name is mandatory")
    @NotEmpty(message = "Name can not be empty")
    @NotNull(message = "Name cannot be null")
    @Email(message = "Invalid email format")
    @JsonProperty
    private String email;

    @NotBlank(message = "Name is mandatory")
    @NotEmpty(message = "Name can not be empty")
    @NotNull(message = "Name cannot be null")
    @Size(min = 5, message = "Password must be at least 8 characters long")
    @JsonProperty
    private String password;

    @JsonProperty
    private Long workCenterId;
}

