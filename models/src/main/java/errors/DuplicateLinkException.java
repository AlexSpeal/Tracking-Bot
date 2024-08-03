package errors;

public class DuplicateLinkException extends RuntimeException {
    public DuplicateLinkException(String massage) {
        super(massage);
    }
}
