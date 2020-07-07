package infrastructure.exceptions;

/**
 * Exception for signal about reflation work exceptions
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class ReflectionException extends RuntimeException {
    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException() {
    }
}
