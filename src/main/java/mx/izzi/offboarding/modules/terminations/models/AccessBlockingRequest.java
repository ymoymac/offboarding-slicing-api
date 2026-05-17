package mx.izzi.offboarding.modules.terminations.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.users.domain.models.User;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccessBlockingRequest {
    private Long requestId;
    private String folio;
    private Date endDate;
    private LocalDateTime applicationDate;
    private String laboraId;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private User user;
    private Employee employee;
    private TerminationType terminationType;
    private TerminationReason terminationReason;
}
