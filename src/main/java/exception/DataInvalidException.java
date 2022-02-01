package exception;

public class DataInvalidException extends RuntimeException {

    private String message;

    public DataInvalidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
