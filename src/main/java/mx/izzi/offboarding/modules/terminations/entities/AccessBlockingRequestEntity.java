package mx.izzi.offboarding.modules.terminations.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.employees.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.terminations.models.AccessBlockingRequest;
import mx.izzi.offboarding.modules.users.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_offboarding_access_blocking_request", schema = "offboarding")
public class AccessBlockingRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", unique = true, nullable = false)
    private Long requestId;

    @Column(name = "req_tx_folio", nullable = false)
    private String folio;

    @Column(name = "req_dt_end_date", nullable = false)
    private Date endDate;

    @Column(name = "req_dt_application_date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(name = "req_tx_labora_id", nullable = false)
    private String laboraId;

    @Column(name = "req_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "req_dt_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "req_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "req_tx_created_by", nullable = false, length = 80)
    private String createdBy;

    @Column(name = "req_tx_updated_by", nullable = false, length = 80)
    private String updatedBy;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "req_fk_user_id",
            referencedColumnName = "user_id",
            nullable = false
    )
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_fk_employee_id", nullable = false)
    private EmployeeEntity employee;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "req_fk_termination_type_id",
            referencedColumnName = "termination_type_id",
            nullable = false
    )
    private TerminationTypeEntity terminationType;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "req_fk_termination_reason_id",
            referencedColumnName = "termination_reason_id",
            nullable = false
    )
    private TerminationReasonEntity terminationReason;

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

    public AccessBlockingRequest toDomain() {
        return AccessBlockingRequest.builder()
                .requestId(this.requestId)
                .folio(this.folio)
                .endDate(this.endDate)
                .applicationDate(this.applicationDate)
                .laboraId(this.laboraId)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .createdBy(this.createdBy)
                .updatedBy(this.updatedBy)
                .user(this.user.toDomain())
                .employee(this.employee.toDomain())
                .terminationType(this.terminationType.toDomain())
                .terminationReason(this.terminationReason.toDomain())
                .build();
    }
}
