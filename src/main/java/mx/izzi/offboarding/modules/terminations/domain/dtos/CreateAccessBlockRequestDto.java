package mx.izzi.offboarding.modules.terminations.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateAccessBlockRequestDto {

    @FutureOrPresent(message = "The end date must be from today onwards")
    @JsonProperty
    private LocalDate endDate;

    @JsonProperty
    private Long immediateBossIdssff;

    @JsonProperty
    private Long employeeIdssff;

    @JsonProperty
    private Long terminationTypeId;

    @JsonProperty
    private Long terminationReasonId;
}
