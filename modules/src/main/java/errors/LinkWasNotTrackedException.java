package errors;

public class LinkWasNotTrackedException extends RuntimeException {
    public LinkWasNotTrackedException(String message) {
        super(message);
    }
}
