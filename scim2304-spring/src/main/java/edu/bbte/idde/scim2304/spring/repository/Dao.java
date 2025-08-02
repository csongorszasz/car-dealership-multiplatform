package edu.bbte.idde.scim2304.spring.repository;


import edu.bbte.idde.scim2304.spring.model.BaseEntity;
import edu.bbte.idde.scim2304.spring.repository.exceptions.EntityAlreadyExistsException;
import edu.bbte.idde.scim2304.spring.repository.exceptions.EntityNotFoundException;
import edu.bbte.idde.scim2304.spring.repository.exceptions.FailedCreateEntityException;

import java.util.Collection;

public interface Dao<T extends BaseEntity> {
    String getModelClassTypeName();  // returns the name of the type "T" is replaced after type erasure

    T create(T entity) throws FailedCreateEntityException, EntityAlreadyExistsException;

    T findById(Long id) throws EntityNotFoundException;

    Collection<T> findAll();

    T update(Long id, T entity) throws EntityNotFoundException;

    T delete(Long id) throws EntityNotFoundException;
}
