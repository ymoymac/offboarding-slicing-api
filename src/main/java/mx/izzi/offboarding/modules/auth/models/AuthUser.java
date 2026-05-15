package mx.izzi.offboarding.modules.auth.models;

import lombok.*;
import mx.izzi.offboarding.modules.users.models.UserEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthUser {
    private UserEntity user;
    private String token;
}
