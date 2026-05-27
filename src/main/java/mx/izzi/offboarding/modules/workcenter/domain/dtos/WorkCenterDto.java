package mx.izzi.offboarding.modules.workcenter.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkCenterDto {

    @JsonProperty
    private String region;

    @JsonProperty
    private String locality;

    @JsonProperty
    private String business;

    @JsonProperty
    private String division;

    @JsonProperty
    private String company;

    @JsonProperty
    private String workCenter;

    @JsonProperty
    private String department;

    @JsonProperty
    private Integer dependents;
}
