package mx.izzi.offboarding.modules.users.models;

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
public class User {
    private Long userId;
    private Long idssff;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private String username;
    private String email;
    private String password;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Role role;
}
