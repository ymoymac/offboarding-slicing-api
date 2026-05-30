package mx.izzi.offboarding.shared.errors;

import lombok.extern.slf4j.Slf4j;
import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.exceptions.*;
import mx.izzi.offboarding.shared.models.OBErrorResponse;
import mx.izzi.offboarding.shared.utils.ErrorMapper;
import org.springframework.dao.DataAccessResourceFailureException;
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

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<OBErrorResponse> handleServerException(ServerException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.INTERNAL_SERVER_ERROR,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.INTERNAL_SERVER_ERROR)
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<OBErrorResponse> handleNumberFormat(NumberFormatException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.NAN, ErrorMapper.errors(OBErrorCodes.NAN)),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ValueNotValidException.class)
    public ResponseEntity<OBErrorResponse> handleValueNotValid(ValueNotValidException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.VALUE_NOT_VALID,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.VALUE_NOT_VALID)
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<OBErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
                return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.UNIQUENESS_RULE, ErrorMapper.errors(OBErrorCodes.UNIQUENESS_RULE)),
                HttpStatus.CONFLICT
        );
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<OBErrorResponse> handleDataIntegrity(ResourceNotFoundException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.NOT_FOUND,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.NOT_FOUND)
                ),
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
    public ResponseEntity<OBErrorResponse> handleAlreadyExists(ResourceAlreadyExistsException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.UNIQUENESS_RULE,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.UNIQUENESS_RULE)
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<OBErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.EMAIL_ALREADY_EXISTS,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.EMAIL_ALREADY_EXISTS)
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ResourceNotAvailableException.class)
    public ResponseEntity<OBErrorResponse> handleResourceNotAvailable(ResourceNotAvailableException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.NOT_AVAILABLE,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.NOT_AVAILABLE)
                ),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(ResourceAlreadyTerminatedException.class)
    public ResponseEntity<OBErrorResponse> handleResourceAlreadyTerminated(ResourceAlreadyTerminatedException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.EMPLOYEE_TERMINATED,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.EMPLOYEE_TERMINATED)
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<OBErrorResponse> handleDataAccessResourceFailure(DataAccessResourceFailureException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.DB_ERROR_CONNECTION,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.DB_ERROR_CONNECTION)
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ResourceRoleAlreadyAssignedException.class)
    public ResponseEntity<OBErrorResponse> handleResourceRoleAlreadyAssigned(ResourceRoleAlreadyAssignedException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.ROLE_RELATION_ALREADY_EXISTS,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.ROLE_RELATION_ALREADY_EXISTS)
                ),
                HttpStatus.BAD_REQUEST
        );
    }

}
