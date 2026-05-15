package mx.izzi.offboarding.modules.workcenter.entities;

import jakarta.persistence.*;
import lombok.*;
import mx.izzi.offboarding.modules.employees.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.users.entities.UserEntity;
import mx.izzi.offboarding.modules.workcenter.models.WorkCenter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_offboarding_work_centers", schema = "offboarding")
public class WorkCenterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "wce_tx_region_id", nullable = false)
    private String regionId;

    @Column(name = "wce_tx_region", nullable = false)
    private String region;

    @Column(name = "wce_tx_locality_id", nullable = false)
    private String localityId;

    @Column(name = "wce_tx_locality", nullable = false)
    private String locality;

    @Column(name = "wce_tx_business_id", nullable = false)
    private String businessId;

    @Column(name = "wce_tx_business", nullable = false)
    private String business;

    @Column(name = "wce_tx_division_id", nullable = false)
    private String divisionId;

    @Column(name = "wce_tx_division", nullable = false)
    private String division;

    @Column(name = "wce_tx_company_id", nullable = false)
    private String companyId;

    @Column(name = "wce_tx_company", nullable = false)
    private String company;

    @Column(name = "wce_tx_work_center_id", nullable = false)
    private String workCenterId;

    @Column(name = "wce_tx_work_center", nullable = false)
    private String workCenter;

    @Column(name = "wce_tx_department_id", nullable = false)
    private String departmentId;

    @Column(name = "wce_tx_department", nullable = false)
    private String department;

    @Column(name = "wce_no_dependents", nullable = false)
    private Integer dependents;

    @Column(name = "wce_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "wce_dt_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "wce_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "wce_tx_created_by", nullable = false)
    private String createdBy;

    @Column(name = "wce_tx_updated_by", nullable = false)
    private String updatedBy;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "workCenter",
            cascade = CascadeType.ALL
    )
    private Set<UserEntity> users;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "workCenter",
            cascade = CascadeType.ALL
    )
    private Set<EmployeeEntity> employees;

    @PrePersist
    private void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public WorkCenter toDomain() {
        return WorkCenter.builder()
                .id(id)
                .regionId(regionId)
                .region(region)
                .localityId(localityId)
                .locality(locality)
                .businessId(businessId)
                .business(business)
                .divisionId(divisionId)
                .division(division)
                .companyId(companyId)
                .company(company)
                .workCenterId(workCenterId)
                .workCenter(workCenter)
                .departmentId(departmentId)
                .department(department)
                .dependents(dependents)
                .isActive(isActive)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build();
    }
}
