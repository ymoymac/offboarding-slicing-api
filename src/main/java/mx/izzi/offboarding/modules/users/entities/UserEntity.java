package mx.izzi.offboarding.modules.users.entities;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.users.models.User;
import mx.izzi.offboarding.modules.workcenter.entities.WorkCenterEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_offboarding_users", schema = "offboarding")
public class UserEntity {
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

    @Column(name = "usr_tx_second_surname", nullable = false, length = 100)
    private String secondSurname;

    @Column(name = "usr_tx_username", nullable = false, length = 100)
    private String username;

    @Column(name = "usr_tx_email", nullable = false)
    private String email;

    @Column(name = "usr_tx_password", nullable = false)
    private String password;

    @Column(name = "usr_st_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "usr_dt_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "usr_dt_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "usr_tx_created_by", nullable = false, length = 80)
    private String createdBy;

    @Column(name = "usr_tx_updated_by", nullable = false, length = 80)
    private String updatedBy;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "usr_fk_role_id",
            referencedColumnName = "role_id",
            nullable = false
    )
    private RoleEntity role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "usr_fk_work_center_id",
            referencedColumnName = "id",
            nullable = false
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
                .role(this.role.toDomain())
                .workCenter(this.workCenter.toDomain())
                .build();
    }

}
