package mx.izzi.offboarding.modules.users.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_offboarding_roles", schema = "offboarding")
public class RoleEntity {
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

    @Column(name = "role_tx_created_by", nullable = false, length = 80)
    private String createdBy;

    @Column(name = "role_tx_updated_by", nullable = false, length = 80)
    private String updatedBy;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "role",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private Set<UserEntity> userEntities;

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
