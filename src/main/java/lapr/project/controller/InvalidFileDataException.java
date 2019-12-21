package lapr.project.controller;

public class InvalidFileDataException extends Exception {
    private final static long serialVersionUID = 100000L;
    public InvalidFileDataException(String message) {
        super(message);
    }
}
