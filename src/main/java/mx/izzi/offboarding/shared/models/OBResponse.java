package mx.izzi.offboarding.shared.models;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class OBResponse<T> {
    private LocalDateTime timestamp;
    private int httpStatus; // -> 200, 201, 404
    private String httpCode; // OK, Created, Unauthorized
    private String message; // Generic message
    private T data;
}
