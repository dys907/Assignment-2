/**
 * Custom exception for wrong upload file type.
 */
public class IncorrectFileTypeException extends Exception {
    public IncorrectFileTypeException(String errorMsg) {
        super (errorMsg);
    }
}
