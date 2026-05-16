package mx.izzi.offboarding.modules.employees.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.users.models.User;
import mx.izzi.offboarding.modules.workcenter.models.WorkCenter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {
    private Long employeeId;
    private Long idssff;
    private Long number;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String status;
    private String jobPositionId;
    private String jobPosition;
    private String positionId;
    private String position;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private User immediateBoos;
    private WorkCenter  workCenter;
}

