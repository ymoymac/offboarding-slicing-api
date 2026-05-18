package mx.izzi.offboarding.shared.exceptions;

public class ResourceAlreadyTerminatedException extends RuntimeException {
    public ResourceAlreadyTerminatedException(String message) {
        super(message);
    }
}
