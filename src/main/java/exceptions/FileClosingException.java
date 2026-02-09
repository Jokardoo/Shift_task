package exceptions;

public class FileClosingException extends RuntimeException {
    public FileClosingException(String message) {
        super(message);
    }
}
