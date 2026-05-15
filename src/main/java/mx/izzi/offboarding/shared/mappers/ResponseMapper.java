package mx.izzi.offboarding.shared.mappers;

import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.enums.OBResponseCodes;
import mx.izzi.offboarding.shared.models.OBResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseMapper {
    public static <T> ResponseEntity<OBResponse<T>> map(OBResponseCodes codes, T data, HttpStatus httpStatus) {
        OBResponse<T> response = OBResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(codes.getHttpStatus())
                .httpCode(codes.getHttpCode())
                .message(codes.getMessage())
                .data(data)
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }

    public static <T> ResponseEntity<OBResponse<T>> toError(OBErrorCodes codes, HttpStatus httpStatus) {
        OBResponse<T> response = OBResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(codes.getHttpStatus())
                .httpCode(codes.getHttpCode())
                .message(codes.getMessage())
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }
}
