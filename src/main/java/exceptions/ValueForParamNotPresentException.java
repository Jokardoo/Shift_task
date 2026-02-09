package exceptions;

public class ValueForParamNotPresentException extends RuntimeException {
    public ValueForParamNotPresentException(String message) {
        super(message);
    }
}
