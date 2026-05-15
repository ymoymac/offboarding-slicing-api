package mx.izzi.offboarding.shared.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OBErrorResponse {
    private LocalDateTime timestamp;
    private int httpStatus; // -> 200, 201, 404
    private String httpCode; // OK, Created, Unauthorized
    private String message; // Generic message
    private String errorCode; // Application code: PB-USR-001
    private String path; // /api/v1/users/1
    private List<?> errors;
}