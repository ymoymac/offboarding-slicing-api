package mx.izzi.offboarding.modules.employees.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateEmployeeDto {

    @JsonProperty
    private Long number;

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
    private Long workCenterId;
}
