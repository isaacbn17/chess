package exception;

public class ResponseException extends Exception {
    @Override
    public String toString() {
        return message;
    }

    final private String message;
    public ResponseException(String message) {
        super(message);
        this.message = message;
    }
}
