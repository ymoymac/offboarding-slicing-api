package mx.izzi.offboarding.modules.terminations.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.terminations.domain.models.TerminationType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_ob_termination_types", schema = "offboarding")
public class TerminationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "termination_type_id", unique = true, nullable = false)
    private Long terminationTypeId;

    @Column(name = "tty_tx_termination_type", unique = true, nullable = false, length = 100)
    private String type;

    @Column(name = "tty_tx_description", length = 100)
    private String description;

    @Column(name = "tty_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "tty_dt_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "tty_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "tty_tx_created_by", length = 80)
    private String createdBy;

    @Column(name = "tty_tx_updated_by", length = 80)
    private String updatedBy;

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

    public TerminationType toDomain() {
        return TerminationType.builder()
                .terminationTypeId(this.terminationTypeId)
                .type(this.type)
                .description(this.description)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .createdBy(this.createdBy)
                .updatedBy(this.updatedBy)
                .build();
    }
}
