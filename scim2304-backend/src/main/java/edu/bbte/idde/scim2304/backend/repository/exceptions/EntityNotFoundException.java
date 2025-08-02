package edu.bbte.idde.scim2304.backend.repository.exceptions;

public class EntityNotFoundException extends RepositoryException {
    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
