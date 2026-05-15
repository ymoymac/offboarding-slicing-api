package mx.izzi.offboarding.modules.users.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class DetailUserDto {

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
}
