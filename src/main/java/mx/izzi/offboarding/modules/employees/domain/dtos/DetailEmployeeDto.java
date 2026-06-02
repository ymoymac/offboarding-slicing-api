package mx.izzi.offboarding.modules.employees.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.workcenter.domain.dtos.WorkCenterDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetailEmployeeDto {

    @JsonProperty
    private Long idssff;

    @JsonProperty
    private Long number;

    @JsonProperty
    private String name;

    @JsonProperty
    private String firstSurname;

    @JsonProperty
    private String secondSurname;

    @JsonProperty
    private String email;

    @JsonProperty
    private String status;

    @JsonProperty
    private String jobPositionId;

    @JsonProperty
    private String jobPosition;

    @JsonProperty
    private String positionId;

    @JsonProperty
    private String position;

    @JsonProperty
    private Long immediateBossId;

    @JsonProperty
    private WorkCenterDto workCenter;
}
