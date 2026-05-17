package mx.izzi.offboarding.shared.exceptions;

public class ValueNotValidException extends RuntimeException {
    public ValueNotValidException(String message) {
        super(message);
    }
}
