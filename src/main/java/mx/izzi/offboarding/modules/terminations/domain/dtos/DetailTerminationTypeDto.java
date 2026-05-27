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
public class DetailTerminationTypeDto {

    @JsonProperty
    private Long terminationTypeId;

    @JsonProperty
    private String type;

    @JsonProperty
    private String description;
}
