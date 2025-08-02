package edu.bbte.idde.scim2304.spring.repository.mem;

import edu.bbte.idde.scim2304.spring.model.BaseEntity;
import edu.bbte.idde.scim2304.spring.repository.Dao;
import edu.bbte.idde.scim2304.spring.repository.exceptions.EntityAlreadyExistsException;
import edu.bbte.idde.scim2304.spring.repository.exceptions.EntityNotFoundException;
import edu.bbte.idde.scim2304.spring.repository.exceptions.FailedCreateEntityException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("mem")
@Slf4j
public abstract class MemDao<T extends BaseEntity> implements Dao<T> {
    @Setter
    protected Class<T> modelClassType;

    protected ConcurrentMap<Long, T> entities = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public String getModelClassTypeName() {
        return modelClassType.getSimpleName();
    }

    @Override
    public T create(T entity) throws FailedCreateEntityException, EntityAlreadyExistsException {
        Long newId = idGenerator.getAndIncrement();
        if (entities.containsKey(newId)) {
            log.error("{} with id {} already exists", getModelClassTypeName(), newId);
            throw new EntityAlreadyExistsException();
        }
        entity.setId(newId);
        entities.put(newId, entity);
        log.info("Created {} with id {}", getModelClassTypeName(), newId);
        return entity;
    }

    @Override
    public T findById(Long id) throws EntityNotFoundException {
        if (!entities.containsKey(id)) {
            log.error("Cannot find {} with id {}", getModelClassTypeName(), id);
            throw new EntityNotFoundException();
        }
        log.debug("Found {} with id {}", getModelClassTypeName(), id);
        return entities.get(id);
    }

    @Override
    public Collection<T> findAll() {
        var result = entities.values();
        log.info("Found {} {}(s)", result.size(), getModelClassTypeName());
        return result;
    }

    @Override
    public T update(Long id, T entity) throws EntityNotFoundException {
        if (!entities.containsKey(id)) {
            log.error("Cannot find {} with id {}", getModelClassTypeName(), id);
            throw new EntityNotFoundException();
        }
        entities.put(id, entity);
        log.debug("Updated {} with id {}", getModelClassTypeName(), id);
        return entity;
    }

    @Override
    public T delete(Long id) throws EntityNotFoundException {
        if (!entities.containsKey(id)) {
            log.error("{} with id {} not found", getModelClassTypeName(), id);
            throw new EntityNotFoundException();
        }
        var entity = entities.get(id);
        entities.remove(id);
        log.debug("Deleted {} with id {}", getModelClassTypeName(), id);
        return entity;
    }
}
