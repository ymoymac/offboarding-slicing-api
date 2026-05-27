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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(
        name = "t_ob_users",
        schema = "offboarding",
        indexes = {
                @Index(name = "i_user_idssff", columnList = "user_idssff", unique = true),
                @Index(name = "i_user_email", columnList = "usr_tx_email", unique = true)
        }
)
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

    @Column(name = "usr_tx_second_surname", length = 100)
    private String secondSurname;

    @Column(name = "usr_tx_username", nullable = false, length = 100)
    private String username;

    @Column(name = "usr_tx_email", unique = true, nullable = false)
    private String email;

    @Column(name = "usr_tx_password", nullable = false)
    private String password;

    @Column(name = "usr_st_is_active")
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
    private Set<UserRoleUnionEntity> roles;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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
                .workCenter(this.workCenter.toDomain())
                .build();
    }

}
