package lapr.project.controller;

public class InvalidFileDataException extends Exception {
    private static final long serialVersionUID = 100000L;
    public InvalidFileDataException(String message) {
        super(message);
    }
}
