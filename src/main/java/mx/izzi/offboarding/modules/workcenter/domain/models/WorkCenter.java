package mx.izzi.offboarding.modules.workcenter.domain.models;

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
public class WorkCenter {
    private String id;
    private String regionId;
    private String region;
    private String localityId;
    private String locality;
    private String businessId;
    private String business;
    private String divisionId;
    private String division;
    private String companyId;
    private String company;
    private String workCenterId;
    private String workCenter;
    private String departmentId;
    private String department;
    private Integer dependents;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
