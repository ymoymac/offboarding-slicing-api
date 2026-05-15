package mx.izzi.offboarding.modules.users.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "offboarding", schema = "t_offboarding_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "user_idssff", nullable = false)
    private Long idssff;

    @Column(name = "usr_tx_name", nullable = false)
    private String name;

    @Column(name = "usr_tx_first_surname", nullable = false)
    private String firstSurname;

    @Column(name = "usr_tx_second_surname", nullable = false)
    private String secondSurname;

    @Column(name = "usr_tx_nickname", nullable = false)
    private String nickname;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "usr_fk_role_id",
            referencedColumnName = "role_id",
            nullable = false
    )
    private RoleEntity roleEntity;

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


}
