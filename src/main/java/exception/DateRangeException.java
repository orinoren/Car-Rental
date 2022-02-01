package exception;

public class DateRangeException extends RuntimeException {

    private final String message;

    public DateRangeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
