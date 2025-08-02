package edu.bbte.idde.scim2304.backend.repository.exceptions;

public class EntityAlreadyExistsException extends RepositoryException {
    public EntityAlreadyExistsException() {
        super();
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
