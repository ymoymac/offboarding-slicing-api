package mx.izzi.offboarding.modules.terminations.domain.dtos;

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
public class DetailTerminationReasonDto {

    @JsonProperty
    private Long terminationReasonId;

    @JsonProperty
    private String terminationReason;

    @JsonProperty
    private String description;
}
