package lapr.project.utils;

public class InvalidFileDataException extends Exception {
    private static final long serialVersionUID = 100000L;
    public InvalidFileDataException(String message) {
        super(message);
    }
}
