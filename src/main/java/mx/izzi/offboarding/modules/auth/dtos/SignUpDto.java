package mx.izzi.offboarding.modules.auth.dtos;

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
public class SignUpDto {
    @JsonProperty
    private Long idssff;

    @NotBlank(message = "Name is mandatory")
    @NotEmpty(message = "Name can not be empty")
    @NotNull(message = "Name cannot be null")
    @JsonProperty
    private String name;

    @NotBlank(message = "FirstSurname is mandatory")
    @NotEmpty(message = "FirstSurname can not be empty")
    @NotNull(message = "FirstSurname cannot be null")
    @JsonProperty
    private String firstSurname;

    @NotBlank(message = "SecondSurname is mandatory")
    @NotEmpty(message = "SecondSurname can not be empty")
    @NotNull(message = "SecondSurname cannot be null")
    @JsonProperty
    private String secondSurname;

    @NotBlank(message = "Email is mandatory")
    @NotEmpty(message = "Email can not be empty")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    @JsonProperty
    private String email;

    @NotBlank(message = "Password is mandatory")
    @NotEmpty(message = "Password can not be empty")
    @NotNull(message = "Password cannot be null")
    @Size(min = 5, message = "Password must be at least 8 characters long")
    @JsonProperty
    private String password;

    @JsonProperty
    private String location;

    @JsonProperty
    private String position;

}
