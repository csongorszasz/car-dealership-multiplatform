package edu.bbte.idde.scim2304.web.exception;

public class CustomServletException extends RuntimeException {
    private final int statusCode;

    public CustomServletException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public CustomServletException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
