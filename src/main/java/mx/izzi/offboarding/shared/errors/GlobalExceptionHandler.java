package mx.izzi.offboarding.shared.errors;

import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.exceptions.ResourceAlreadyExistsException;
import mx.izzi.offboarding.shared.exceptions.ResourceNotFoundException;
import mx.izzi.offboarding.shared.models.OBErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice()
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<OBErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException cve) {
            String constraint = cve.getConstraintName();

            if (constraint != null) {
                String column = constraint.contains(".")
                        ? constraint.split("\\.")[1]
                        : constraint.toLowerCase();

                List<Map<String, String>> errors = new ArrayList<>();
                Map<String, String> err = new HashMap<>();
                String message = switch (column) {
                    case ""  -> "This email is already registered";
                    case "UserMySQLQueries.IDSSFF" -> "This SuccessFactor ID is already registered.";
                    default -> "Integrity restriction violated: " + constraint;
                };
                err.put("message", message);
                errors.add(err);

                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new OBErrorResponse(OBErrorCodes.USER_UNIQUENESS_RULE, errors));
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new OBErrorResponse(OBErrorCodes.USER_UNIQUENESS_RULE));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<OBErrorResponse> handleDataIntegrity(ResourceNotFoundException e) {
        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("errorCode", "PB-USR-ERR001");
        err.put("message", "User not found");
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.USER_NOT_FOUND, errors),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OBErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<Map<String, Object>> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            Map<String, Object> err = new HashMap<>();
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            err.put("field", fieldName);
            err.put("message", errorMessage);
            err.put("rejectedValue", rejectedValue);
            errors.add(err);
        });

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.USER_ERROR_PARAMS, errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<OBErrorResponse> handleAlreadyExists(ResourceAlreadyExistsException ex) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("message", "This user whit this idssff already exists");
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.USER_UNIQUENESS_RULE, errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<OBErrorResponse> handleBadCredentials(BadCredentialsException ex) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("errorCode", "PB-AUTH-ERR001");
        err.put("message", "User or password is incorrect");
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.LOGIN_ERROR_CREDENTIALS, errors),
                HttpStatus.UNAUTHORIZED
        );
    }

}
