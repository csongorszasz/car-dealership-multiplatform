package edu.bbte.idde.scim2304.spring.repository.exceptions;

public class FailedCreateEntityException extends RepositoryException {
    public FailedCreateEntityException() {
        super();
    }

    public FailedCreateEntityException(String message) {
        super(message);
    }

    public FailedCreateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
