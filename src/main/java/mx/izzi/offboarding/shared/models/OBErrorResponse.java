package mx.izzi.offboarding.shared.models;

import lombok.*;
import mx.izzi.offboarding.shared.enums.OBErrorCodes;

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

    public OBErrorResponse(OBErrorCodes codes) {
        this.timestamp = LocalDateTime.now();
        this.httpStatus = codes.getHttpStatus();
        this.httpCode = codes.getHttpCode();
        this.message = codes.getMessage();
    }

    public OBErrorResponse(OBErrorCodes codes, List<?> errors) {
        this.timestamp = LocalDateTime.now();
        this.httpStatus = codes.getHttpStatus();
        this.httpCode = codes.getHttpCode();
        this.message = codes.getMessage();
        this.errors = errors;
    }
}