package mx.izzi.offboarding.modules.terminations.domain.dtos;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.employees.domain.dtos.DetailEmployeeDto;
import mx.izzi.offboarding.modules.users.domain.dtos.DetailUserDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetailAccessBlockRequestDto {

    @JsonProperty
    private Long requestId;

    @JsonProperty
    private String folio;

    @JsonProperty
    private Date endDate;

    @JsonProperty
    private LocalDateTime applicationDate;

    @JsonProperty
    private DetailUserDto user;

    @JsonProperty
    private DetailUserDto immediateBoss;

    @JsonProperty
    private DetailEmployeeDto employee;

    @JsonProperty
    private DetailTerminationTypeDto terminationType;

    @JsonProperty
    private DetailTerminationReasonDto terminationReason;
}
