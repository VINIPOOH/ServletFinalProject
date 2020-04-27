package exeptions;

public class PasswordEncodeException extends RuntimeException {
    public PasswordEncodeException(String message) {
        super(message);
    }
}
