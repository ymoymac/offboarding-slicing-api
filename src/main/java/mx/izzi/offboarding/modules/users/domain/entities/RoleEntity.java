package mx.izzi.offboarding.modules.users.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import mx.izzi.offboarding.modules.users.domain.models.Role;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "t_ob_roles",
        schema = "offboarding",
        indexes = {
                @Index(name = "i_role_name", columnList = "role_tx_name", unique = true)
        }
)
public class RoleEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", unique = true, nullable = false)
    private Long roleId;

    @Column(name = "role_tx_name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "role_tx_description", unique = true, nullable = false)
    private String description;

    @Column(name = "role_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "role_dt_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "role_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "role_tx_created_by", length = 80)
    private String createdBy;

    @Column(name = "role_tx_updated_by", length = 80)
    private String updatedBy;

    @OneToMany(
            mappedBy = "role",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<RoleUserUnionEntity> users;

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

    public Role  toDomain() {
        return Role.builder()
                .roleId(this.roleId)
                .name(this.name)
                .description(this.description)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .createdBy(this.createdBy)
                .updatedBy(this.updatedBy)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RoleEntity that)) return false;

        return new EqualsBuilder()
                .append(roleId, that.roleId)
                .append(name, that.name)
                .append(description, that.description)
                .append(isActive, that.isActive)
                .append(createdAt, that.createdAt)
                .append(updatedAt, that.updatedAt)
                .append(createdBy, that.createdBy)
                .append(updatedBy, that.updatedBy)
                .append(users, that.users)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(roleId)
                .append(name)
                .append(description)
                .append(isActive)
                .append(createdAt)
                .append(updatedAt)
                .append(createdBy)
                .append(updatedBy)
                .append(users)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("roleId", roleId)
                .append("name", name)
                .append("description", description)
                .append("isActive", isActive)
                .append("createdAt", createdAt)
                .append("updatedAt", updatedAt)
                .append("createdBy", createdBy)
                .append("updatedBy", updatedBy)
                .append("users", users)
                .toString();
    }
}
