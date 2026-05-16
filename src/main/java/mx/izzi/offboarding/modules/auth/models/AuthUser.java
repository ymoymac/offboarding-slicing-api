package mx.izzi.offboarding.modules.auth.models;

import lombok.*;
import mx.izzi.offboarding.modules.users.domain.models.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthUser {
    private User user;
    private String token;
}
