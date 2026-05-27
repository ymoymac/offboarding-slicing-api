package mx.izzi.offboarding.modules.users.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import mx.izzi.offboarding.modules.users.domain.models.RoleUserUnion;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "t_ob_users",
        schema = "offboarding",
        indexes = {
                @Index(name = "i_user_idssff", columnList = "user_idssff", unique = true),
                @Index(name = "i_user_email", columnList = "usr_tx_email", unique = true)
        }
)
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "user_idssff", nullable = false)
    private Long idssff;

    @Column(name = "usr_tx_name", nullable = false, length = 100)
    private String name;

    @Column(name = "usr_tx_first_surname", nullable = false, length = 100)
    private String firstSurname;

    @Column(name = "usr_tx_second_surname", length = 100)
    private String secondSurname;

    @Column(name = "usr_tx_username", nullable = false, length = 100)
    private String username;

    @Column(name = "usr_tx_email", unique = true, nullable = false)
    private String email;

    @Column(name = "usr_tx_password", nullable = false)
    private String password;

    @Column(name = "usr_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "usr_dt_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "usr_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "usr_tx_created_by", length = 80)
    private String createdBy;

    @Column(name = "usr_tx_updated_by", length = 80)
    private String updatedBy;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<RoleUserUnionEntity> roleUserUnion;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "usr_fk_work_center_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_users_to_work_centers")
    )
    private WorkCenterEntity workCenter;

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

    public User toDomain() {
        return User.builder()
                .userId(this.userId)
                .idssff(this.idssff)
                .name(this.name)
                .firstSurname(this.firstSurname)
                .secondSurname(this.secondSurname)
                .password(this.password)
                .username(this.username)
                .email(this.email)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .createdBy(this.createdBy)
                .updatedBy(this.updatedBy)
                .roles(
                        this.roleUserUnion == null ? new ArrayList<>() : this.roleUserUnion.stream()
                                .map(RoleUserUnionEntity::toDomain)
                                .map(RoleUserUnion::getRole)
                                .toList()
                )
                .workCenter(this.workCenter.toDomain())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UserEntity that)) return false;

        return new EqualsBuilder()
                .append(userId, that.userId)
                .append(idssff, that.idssff)
                .append(name, that.name)
                .append(firstSurname, that.firstSurname)
                .append(secondSurname, that.secondSurname)
                .append(username, that.username)
                .append(email, that.email)
                .append(password, that.password)
                .append(isActive, that.isActive)
                .append(createdAt, that.createdAt)
                .append(updatedAt, that.updatedAt)
                .append(createdBy, that.createdBy)
                .append(updatedBy, that.updatedBy)
                .append(roleUserUnion, that.roleUserUnion)
                .append(workCenter, that.workCenter)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userId)
                .append(idssff)
                .append(name)
                .append(firstSurname)
                .append(secondSurname)
                .append(username)
                .append(email)
                .append(password)
                .append(isActive)
                .append(createdAt)
                .append(updatedAt)
                .append(createdBy)
                .append(updatedBy)
                .append(roleUserUnion)
                .append(workCenter)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("idssff", idssff)
                .append("name", name)
                .append("firstSurname", firstSurname)
                .append("secondSurname", secondSurname)
                .append("username", username)
                .append("email", email)
                .append("password", password)
                .append("isActive", isActive)
                .append("createdAt", createdAt)
                .append("updatedAt", updatedAt)
                .append("createdBy", createdBy)
                .append("updatedBy", updatedBy)
                .append("roleUserUnion", roleUserUnion)
                .append("workCenter", workCenter)
                .toString();
    }
}
