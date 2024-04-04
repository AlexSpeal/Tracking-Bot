package errors;

public class ChatNotExistsException extends RuntimeException {
    public ChatNotExistsException(String message) {
        super(message);
    }
}
