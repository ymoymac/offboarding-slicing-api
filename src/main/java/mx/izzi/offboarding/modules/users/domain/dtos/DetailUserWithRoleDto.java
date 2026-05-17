package mx.izzi.offboarding.modules.users.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetailUserWithRoleDto {

    @JsonProperty
    private Long idssff;

    @JsonProperty
    private String name;

    @JsonProperty
    private String firstSurname;

    @JsonProperty
    private String secondSurname;

    @JsonProperty
    private String nickname;

    @JsonProperty
    private String email;

    @JsonProperty
    private Boolean isActive;

    @JsonProperty
    private DetailRoleDto role;
}
