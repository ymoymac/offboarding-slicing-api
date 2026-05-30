package mx.izzi.offboarding.modules.users.domain.dtos;

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
public class AssignRoleDto {

    @JsonProperty
    private Long  roleId;

    @JsonProperty
    private Long userIdssff;
}
