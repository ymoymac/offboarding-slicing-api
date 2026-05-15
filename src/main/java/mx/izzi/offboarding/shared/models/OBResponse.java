package mx.izzi.offboarding.shared.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OBResponse<T> {
    private LocalDateTime timestamp;
    private int httpStatus; // -> 200, 201, 404
    private String httpCode; // OK, Created, Unauthorized
    private String message; // Generic message
    private T data;
}
