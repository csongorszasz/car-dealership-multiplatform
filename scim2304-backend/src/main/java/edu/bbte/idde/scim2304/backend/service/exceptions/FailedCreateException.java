package edu.bbte.idde.scim2304.backend.service.exceptions;

public class FailedCreateException extends Exception {
    public FailedCreateException() {
        super();
    }

    public FailedCreateException(String message) {
        super(message);
    }

    public FailedCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
