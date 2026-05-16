package mx.izzi.offboarding.modules.employees.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import mx.izzi.offboarding.modules.employees.models.Employee;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.workcenter.entities.WorkCenterEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_offboarding_employees", schema = "offboarding")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true, nullable = false)
    private Long employeeId;

    @Column(name = "employee_idssff", nullable = false)
    private Long idssff;

    @Column(name = "emp_no_number", nullable = false)
    private Long number;

    @Column(name = "emp_tx_name", nullable = false, length = 100)
    private String name;

    @Column(name = "emp_tx_first_surname", nullable = false, length = 100)
    private String firstSurname;

    @Column(name = "emp_tx_second_surname", nullable = false, length = 100)
    private String secondSurname;

    @Column(name = "emp_tx_email", nullable = false, length = 100)
    private String email;

    @Column(name = "emp_tx_status", nullable = false, length = 80)
    private String status;

    @Column(name = "emp_tx_job_position_id", nullable = false, length = 80)
    private String jobPositionId;

    @Column(name = "emp_tx_job_position", nullable = false, length = 80)
    private String jobPosition;

    @Column(name = "emp_tx_position_id", nullable = false, length = 80)
    private String positionId;

    @Column(name = "emp_tx_position", nullable = false, length = 100)
    private String position;

    @Column(name = "emp_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "emp_dt_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "emp_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "emp_tx_created_by", nullable = false, length = 80)
    private String createdBy;

    @Column(name = "emp_tx_updated_by", nullable = false, length = 80)
    private String updatedBy;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "emp_fk_immediate_boss",
            referencedColumnName = "user_id",
            nullable = false
    )
    private UserEntity immediateBoss;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "emp_fk_work_center_id",
            referencedColumnName = "id",
            nullable = false
    )
    private WorkCenterEntity workCenter;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Employee toDomain() {
        return Employee.builder()
                .employeeId(this.employeeId)
                .idssff(this.idssff)
                .number(this.number)
                .name(this.name)
                .firstSurname(this.firstSurname)
                .secondSurname(this.secondSurname)
                .email(this.email)
                .status(this.status)
                .jobPositionId(this.jobPositionId)
                .jobPosition(this.jobPosition)
                .positionId(this.positionId)
                .position(this.position)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .createdBy(this.createdBy)
                .updatedBy(this.updatedBy)
                .immediateBoos(this.immediateBoss.toDomain())
                .workCenter(this.workCenter.toDomain())
                .build();
    }


}
