package edu.bbte.idde.scim2304.backend.repository.mem;

import edu.bbte.idde.scim2304.backend.repository.Dao;
import edu.bbte.idde.scim2304.backend.repository.exceptions.EntityAlreadyExistsException;
import edu.bbte.idde.scim2304.backend.repository.exceptions.EntityNotFoundException;
import edu.bbte.idde.scim2304.backend.repository.exceptions.FailedCreateEntityException;
import edu.bbte.idde.scim2304.backend.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class MemDao<T extends BaseEntity> implements Dao<T> {
    private static final Logger LOG = LoggerFactory.getLogger(MemDao.class);

    protected final Class<T> modelClassType;
    protected ConcurrentMap<Long, T> entities = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(0);

    public MemDao(Class<T> modelClassType) {
        this.modelClassType = modelClassType;
    }

    @Override
    public String getModelClassTypeName() {
        return modelClassType.getSimpleName();
    }

    @Override
    public T create(T entity) throws FailedCreateEntityException, EntityAlreadyExistsException {
        Long newId = idGenerator.getAndIncrement();
        if (entities.containsKey(newId)) {
            LOG.error("{} with id {} already exists", getModelClassTypeName(), newId);
            throw new EntityAlreadyExistsException();
        }
        entity.setId(newId);
        entities.put(newId, entity);
        LOG.info("Created {} with id {}", getModelClassTypeName(), newId);
        return entity;
    }

    @Override
    public T findById(Long id) throws EntityNotFoundException {
        if (!entities.containsKey(id)) {
            LOG.error("Cannot find {} with id {}", getModelClassTypeName(), id);
            throw new EntityNotFoundException();
        }
        LOG.debug("Found {} with id {}", getModelClassTypeName(), id);
        return entities.get(id);
    }

    @Override
    public Collection<T> findAll() {
        var result = entities.values();
        LOG.info("Found {} {}(s)", result.size(), getModelClassTypeName());
        return result;
    }

    @Override
    public T update(Long id, T entity) throws EntityNotFoundException {
        if (!entities.containsKey(id)) {
            LOG.error("Cannot find {} with id {}", getModelClassTypeName(), id);
            throw new EntityNotFoundException();
        }
        entities.put(id, entity);
        LOG.debug("Updated {} with id {}", getModelClassTypeName(), id);
        return entity;
    }

    @Override
    public T delete(Long id) throws EntityNotFoundException {
        if (!entities.containsKey(id)) {
            LOG.error("{} with id {} not found", getModelClassTypeName(), id);
            throw new EntityNotFoundException();
        }
        var entity = entities.get(id);
        entities.remove(id);
        LOG.debug("Deleted {} with id {}", getModelClassTypeName(), id);
        return entity;
    }
}
