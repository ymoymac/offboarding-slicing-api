package mx.izzi.offboarding.modules.users.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "t_ob_roles_users_union",
        schema = "offboarding",
        indexes = {
                @Index(name = "i_union_role_id", columnList = "ruu_fk_role_id"),
                @Index(name = "i_union_user_id", columnList = "ruu_fk_user_id")
        }
)
public class RoleUserUnionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "ruu_fk_role_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_roles_to_union")
    )
    private RoleEntity role;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "ruu_fk_user_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_users_to_union")
    )
    private UserEntity user;

    @Column(name = "ruu_dt_assignment_date", nullable = false)
    private LocalDateTime assignmentDate;

    @Column(name = "ruu_tx_assigned_by", nullable = false, length = 80)
    private String assignedBy;

    @Column(name = "ruu_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "ruu_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "ruu_tx_created_by", length = 80)
    private String createdBy;

    @Column(name = "ruu_tx_updated_by", length = 80)
    private String updatedBy;

    @PrePersist
    private void prePersist() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
