package mx.izzi.offboarding.shared.errors;

import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.exceptions.*;
import mx.izzi.offboarding.shared.models.OBErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<OBErrorResponse> handleServerException(ServerException e) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.INTERNAL_SERVER_ERROR.getCode());
        err.put("display", OBErrorCodes.INTERNAL_SERVER_ERROR.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.INTERNAL_SERVER_ERROR, e.getMessage(), errors),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<OBErrorResponse> handleNumberFormat(NumberFormatException e) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.NAN.getCode());
        err.put("display", OBErrorCodes.NAN.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.NAN, errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ValueNotValidException.class)
    public ResponseEntity<OBErrorResponse> handleValueNotValid(ValueNotValidException e) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.VALUE_NOT_VALID.getCode());
        err.put("display", OBErrorCodes.VALUE_NOT_VALID.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.VALUE_NOT_VALID, e.getMessage(), errors),
                HttpStatus.BAD_REQUEST
        );
    }

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
                    case "t_offboarding_employees.employee_idssff" -> "This SuccessFactor ID is already registered.";
                    default -> "Integrity restriction violated: " + constraint;
                };
                err.put("code", OBErrorCodes.UNIQUENESS_RULE.getCode());
                err.put("message", message);
                errors.add(err);

                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new OBErrorResponse(OBErrorCodes.UNIQUENESS_RULE, errors));
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new OBErrorResponse(OBErrorCodes.UNIQUENESS_RULE));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<OBErrorResponse> handleDataIntegrity(ResourceNotFoundException e) {
        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.NOT_FOUND.getCode());
        err.put("display", OBErrorCodes.NOT_FOUND.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.NOT_FOUND, e.getMessage(), errors),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OBErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {

        List<Map<String, Object>> errors = new ArrayList<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            Map<String, Object> err = new HashMap<>();
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            err.put("code", OBErrorCodes.INVALID_BODY_PARAMS.getCode());
            err.put("display", OBErrorCodes.INVALID_BODY_PARAMS.getDisplay());
            err.put("field", fieldName);
            err.put("message", errorMessage);
            assert rejectedValue != null;
            err.put("rejectedValue", rejectedValue.toString().isEmpty() ? "Sin valor" : rejectedValue.toString());
            errors.add(err);
        });

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.INVALID_BODY_PARAMS, errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<OBErrorResponse> handleAlreadyExists(ResourceAlreadyExistsException ex) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.UNIQUENESS_RULE.getCode());
        err.put("display", OBErrorCodes.UNIQUENESS_RULE.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.UNIQUENESS_RULE, errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceNotAvailableException.class)
    public ResponseEntity<OBErrorResponse> handleResourceNotAvailable(ResourceNotAvailableException e) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.NOT_AVAILABLE.getCode());
        err.put("display", OBErrorCodes.NOT_AVAILABLE.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.NOT_AVAILABLE, e.getMessage(), errors),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(ResourceAlreadyTerminatedException.class)
    public ResponseEntity<OBErrorResponse> handleResourceAlreadyTerminated(ResourceAlreadyTerminatedException e) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.EMPLOYEE_TERMINATED.getCode());
        err.put("display", OBErrorCodes.EMPLOYEE_TERMINATED.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.EMPLOYEE_TERMINATED, e.getMessage(), errors),
                HttpStatus.BAD_REQUEST
        );
    }


}
