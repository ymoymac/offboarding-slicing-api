package mx.izzi.offboarding.modules.users.domain.dtos;

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
public class DetailRoleUserUnionDto {

    @JsonProperty
    private DetailRoleDto role;

    @JsonProperty
    private DetailUserWithRoleDto user;

    @JsonProperty
    private LocalDateTime assignmentDate;

    @JsonProperty
    private String assignedBy;
}
