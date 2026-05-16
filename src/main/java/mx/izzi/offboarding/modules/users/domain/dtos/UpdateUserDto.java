package mx.izzi.offboarding.modules.users.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
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
public class UpdateUserDto {

    @Size(min = 5, message = "Password must be at least 8 characters long")
    @JsonProperty
    private String password;

    @JsonProperty
    private Long workCenterId;
}
