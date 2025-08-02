package edu.bbte.idde.scim2304.backend.repository;

import edu.bbte.idde.scim2304.backend.repository.exceptions.EntityAlreadyExistsException;
import edu.bbte.idde.scim2304.backend.repository.exceptions.EntityNotFoundException;
import edu.bbte.idde.scim2304.backend.repository.exceptions.FailedCreateEntityException;
import edu.bbte.idde.scim2304.backend.model.BaseEntity;

import java.util.Collection;

public interface Dao<T extends BaseEntity> {
    String getModelClassTypeName();  // returns the name of the type "T" is replaced after type erasure

    T create(T entity) throws FailedCreateEntityException, EntityAlreadyExistsException;

    T findById(Long id) throws EntityNotFoundException;

    Collection<T> findAll();

    T update(Long id, T entity) throws EntityNotFoundException;

    T delete(Long id) throws EntityNotFoundException;
}
