package mx.izzi.offboarding.modules.users.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.workcenter.domain.dtos.WorkCenterDto;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;

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
    private String username;

    @JsonProperty
    private String email;

    @JsonProperty
    private Boolean isActive;

    @JsonProperty
    private WorkCenterDto workCenter;
}
