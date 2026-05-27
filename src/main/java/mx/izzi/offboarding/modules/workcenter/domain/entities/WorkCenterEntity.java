package mx.izzi.offboarding.modules.workcenter.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.employees.domain.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_ob_work_centers", schema = "offboarding")
public class WorkCenterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "wce_tx_region_id", length = 80)
    private String regionId;

    @Column(name = "wce_tx_region", length = 80)
    private String region;

    @Column(name = "wce_tx_locality_id", length = 80)
    private String localityId;

    @Column(name = "wce_tx_locality", length = 80)
    private String locality;

    @Column(name = "wce_tx_business_id", length = 80)
    private String businessId;

    @Column(name = "wce_tx_business", length = 80)
    private String business;

    @Column(name = "wce_tx_division_id", length = 80)
    private String divisionId;

    @Column(name = "wce_tx_division", length = 80)
    private String division;

    @Column(name = "wce_tx_company_id", length = 80)
    private String companyId;

    @Column(name = "wce_tx_company", length = 80)
    private String company;

    @Column(name = "wce_tx_work_center_id", length = 80)
    private String workCenterId;

    @Column(name = "wce_tx_work_center", length = 80)
    private String workCenter;

    @Column(name = "wce_tx_department_id", length = 80)
    private String departmentId;

    @Column(name = "wce_tx_department", length = 80)
    private String department;

    @Column(name = "wce_no_dependents")
    private Integer dependents;

    @Column(name = "wce_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "wce_dt_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "wce_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "wce_tx_created_by", length = 80)
    private String createdBy;

    @Column(name = "wce_tx_updated_by",  length = 80)
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
                .id(this.id)
                .regionId(this.regionId)
                .region(this.region)
                .localityId(this.localityId)
                .locality(this.locality)
                .businessId(this.businessId)
                .business(this.business)
                .divisionId(this.divisionId)
                .division(this.division)
                .companyId(this.companyId)
                .company(this.company)
                .workCenterId(this.workCenterId)
                .workCenter(this.workCenter)
                .departmentId(this.departmentId)
                .department(this.department)
                .dependents(this.dependents)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .createdBy(this.createdBy)
                .updatedBy(this.updatedBy)
                .build();
    }
}
