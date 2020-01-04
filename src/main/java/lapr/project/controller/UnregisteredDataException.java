package lapr.project.controller;

public class UnregisteredDataException extends Exception {
    private static final long serialVersionUID = 100001L;
    public UnregisteredDataException(String message) {
        super(message);
    }
}
