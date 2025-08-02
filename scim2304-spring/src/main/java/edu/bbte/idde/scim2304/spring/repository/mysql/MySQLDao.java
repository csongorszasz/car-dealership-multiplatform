package edu.bbte.idde.scim2304.spring.repository.mysql;

import edu.bbte.idde.scim2304.spring.config.DataSourceConfiguration;
import edu.bbte.idde.scim2304.spring.model.BaseEntity;
import edu.bbte.idde.scim2304.spring.repository.Dao;
import edu.bbte.idde.scim2304.spring.repository.exceptions.EntityAlreadyExistsException;
import edu.bbte.idde.scim2304.spring.repository.exceptions.EntityNotFoundException;
import edu.bbte.idde.scim2304.spring.repository.exceptions.FailedCreateEntityException;
import edu.bbte.idde.scim2304.spring.repository.exceptions.RepositoryException;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
@Profile("jdbc")
public abstract class MySQLDao<T extends BaseEntity> implements Dao<T> {
    protected static final Logger LOG = LoggerFactory.getLogger(MySQLDao.class);

    @Setter
    protected Class<T> modelClass;

    @Setter
    protected List<Field> fields;

    @Autowired
    protected ConnectionPool connectionPool;

    @Autowired
    protected DataSourceConfiguration mySqlConfiguration;

    @Autowired
    protected MySQLHelper mySQLHelper;

    @Override
    public String getModelClassTypeName() {
        return modelClass.getSimpleName();
    }

    @Override
    public T create(T entity) throws FailedCreateEntityException, EntityAlreadyExistsException {
        String inserter = mySQLHelper.buildInsertSql(this);
        LOG.info("Prepared SQL statement: '{}'", inserter);

        // Replace placeholders with the actual values
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(inserter, Statement.RETURN_GENERATED_KEYS)) {

            // Populate the statement with entity values and execute the insertion
            setPreparedStatementValues(stmt, entity);
            executeInsertStatement(stmt);

            // Obtain and set the ID of the newly created entity
            setGeneratedId(stmt, entity);

        } catch (SQLException e) {
            LOG.error("SQL Exception when inserting {}", getModelClassTypeName(), e);
            throw new FailedCreateEntityException("Database error occurred during entity creation", e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOG.error("Unexpected error when inserting {}", getModelClassTypeName(), e);
            throw new FailedCreateEntityException("Failed to create entity due to unexpected error", e);
        }
        return entity;
    }

    /**
      * Helper method to set values in a PreparedStatement.
      */
    private void setPreparedStatementValues(PreparedStatement stmt, T entity)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {
        int parameterIndex = 1;
        for (var field : fields) {
            // Use the entity's getter method to retrieve values dynamically
            Method getter = modelClass.getDeclaredMethod(
                    "get" + mySQLHelper.capitalizeFirstLetter(field.getName())
            );
            Object value = getter.invoke(entity);
            stmt.setObject(parameterIndex++, value);
        }
    }

    /**
      * Helper method to execute an insert statement and check affected rows.
      */
    private static void executeInsertStatement(PreparedStatement stmt) throws
            SQLException, FailedCreateEntityException {
        LOG.info("Executing statement '{}'", stmt);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            LOG.error("Failed to create entity, no rows affected");
            throw new FailedCreateEntityException("Failed to create entity, no rows affected");
        }
        LOG.info("Inserted entity");
    }

    /**
      * Helper method to retrieve and set generated ID.
      */
    private void setGeneratedId(PreparedStatement stmt, T entity) throws SQLException, FailedCreateEntityException {
        try (var generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                entity.setId(generatedId);
                LOG.info("Entity created with ID: {}", generatedId);
            } else {
                LOG.error("Failed to create entity, no ID obtained");
                throw new FailedCreateEntityException("Failed to create entity, no ID obtained");
            }
        }
    }

    @Override
    public T findById(Long id) throws EntityNotFoundException {
        // Build the SQL SELECT command
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(getModelClassTypeName())
            .append(" WHERE id = ?");
        LOG.info("Prepared SQL statement: '{}'", query);

        // Execute the statement and retrieve the entity
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(query.toString())) {
            stmt.setLong(1, id);
            LOG.info("Executing statement '{}'", stmt);

            // Execute the query and process the result set
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    LOG.error("Entity with ID {} not found", id);
                    throw new EntityNotFoundException();
                }

                // Instantiate and populate the entity with the values from the result set
                T entity = populateEntityFromResultSet(resultSet);
                LOG.info("Found and populated entity with ID {}", id);
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception when finding entity with ID {}", id, e);
            throw new RepositoryException("Database error during entity retrieval", e);
        } catch (ReflectiveOperationException e) {
            LOG.error("Reflection error when populating entity with ID {}", id, e);
            throw new RepositoryException("Failed to retrieve entity due to unexpected error", e);
        }
    }

    /**
      * Helper method to populate an entity from a ResultSet.
      */
    private T populateEntityFromResultSet(ResultSet resultSet)
            throws ReflectiveOperationException, SQLException {
        T entity = modelClass.getConstructor().newInstance();
        for (var field : fields) {
            String fieldName = field.getName();

            // Obtain and invoke the setter for each field
            LOG.debug("Obtaining and invoking setter for field '{}' with type '{}'", fieldName, field.getType());
            Method setter = modelClass.getDeclaredMethod(
                    "set" + mySQLHelper.capitalizeFirstLetter(fieldName),
                    field.getType()
            );
            setter.invoke(entity, resultSet.getObject(fieldName, field.getType()));
        }
        // Set the ID separately
        entity.setId(resultSet.getLong("id"));
        return entity;
    }

    @Override
    public Collection<T> findAll() {
        // Build the SQL SELECT command
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(getModelClassTypeName());
        LOG.info("Prepared SQL statement: '{}'", query);

        // Execute the statement and retrieve the entities
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(query.toString())) {
            LOG.info("Executing statement '{}'", stmt);

            // Execute the query and process the result set
            return executeAndPopulateEntities(stmt);
        } catch (SQLException e) {
            LOG.error("SQL Exception when finding all entities", e);
            throw new RepositoryException("Database error during entity retrieval", e);
        } catch (ReflectiveOperationException e) {
            LOG.error("Reflection error when populating entities", e);
            throw new RepositoryException("Failed to retrieve entities due to unexpected error", e);
        }
    }

    /**
     * Generic method to find entities by a given field.
     */
    protected Collection<T> findByField(Field field, Object value) {
        // Build the SQL SELECT command
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(getModelClassTypeName())
            .append(" WHERE ")
            .append(field.getName())
            .append(" = ?");
        return executeSearchAndGetEntities(field, value, query);
    }

    /**
     * Generic method to find entities by comparing them to a given value.
     * *Note: This method is only applicable to fields that are comparable.
     */
    protected Collection<T> findLessThanField(Field field, Object value) {
        // Build the SQL SELECT command
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(getModelClassTypeName())
            .append(" WHERE ")
            .append(field.getName())
            .append(" < ?");
        return executeSearchAndGetEntities(field, value, query);
    }

    protected Collection<T> findGreaterThanField(Field field, Object value) {
        // Build the SQL SELECT command
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(getModelClassTypeName())
            .append(" WHERE ")
            .append(field.getName())
            .append(" > ?");
        return executeSearchAndGetEntities(field, value, query);
    }

    private List<T> executeSearchAndGetEntities(Field field, Object value, StringBuilder query) {
        LOG.info("Prepared SQL statement: '{}'", query);

        // Execute the statement and retrieve the entities
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(query.toString())) {
            stmt.setObject(1, value);
            LOG.info("Executing statement '{}'", stmt);

            // Execute the query and process the result set
            return executeAndPopulateEntities(stmt);
        } catch (SQLException e) {
            LOG.error("SQL Exception when finding entities by field '{}'", field.getName(), e);
            throw new RepositoryException("Database error during entity retrieval", e);
        } catch (ReflectiveOperationException e) {
            LOG.error("Reflection error when populating entities", e);
            throw new RepositoryException("Failed to retrieve entities due to unexpected error", e);
        }
    }

    /**
     * Helper method to execute a statement and populate entities from the result set.
     */
    private List<T> executeAndPopulateEntities(PreparedStatement stmt) throws
            SQLException, ReflectiveOperationException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            List<T> entities = new ArrayList<>();
            while (resultSet.next()) {
                T entity = populateEntityFromResultSet(resultSet);
                entities.add(entity);
            }
            LOG.info("Found and populated {} entities", entities.size());
            return entities;
        }
    }

    @Override
    public T update(Long id, T entity) throws EntityNotFoundException {
        // Build the SQL UPDATE command
        StringBuilder updater = new StringBuilder("UPDATE ")
            .append(getModelClassTypeName())
            .append(" SET ");

        // Append field names and placeholders dynamically
        for (var field : fields) {
            updater.append(field.getName()).append(" = ?, ");
        }
        updater.delete(updater.length() - 2, updater.length());  // Remove trailing comma and space

        // Append the WHERE clause
        updater.append(" WHERE id = ?");
        LOG.info("Prepared SQL statement: '{}'", updater);

        // Replace placeholders with the actual values
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(updater.toString())) {

            // Populate the statement with entity values
            setPreparedStatementValues(stmt, entity);
            stmt.setLong(fields.size() + 1, id);  // Set the ID as the last parameter

            // Execute the statement
            LOG.info("Executing statement '{}'", stmt);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                LOG.error("Failed to update entity with ID {}", id);
                throw new EntityNotFoundException();
            }
            LOG.info("Updated entity with ID {}", id);
        } catch (SQLException e) {
            LOG.error("SQL Exception when updating entity with ID {}", id, e);
            throw new RepositoryException("Database error during entity update", e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOG.error("Unexpected error when updating entity with ID {}", id, e);
            throw new RepositoryException("Failed to update entity due to unexpected error", e);
        }
        return entity;
    }

    @Override
    public T delete(Long id) throws EntityNotFoundException {
        // Retrieve the entity before deletion
        var entity = findById(id);

        // Build the SQL DELETE command
        StringBuilder deleter = new StringBuilder("DELETE FROM ")
            .append(getModelClassTypeName())
            .append(" WHERE id = ?");
        LOG.info("Prepared SQL statement: '{}'", deleter);

        // Execute the statement
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(deleter.toString())) {
            stmt.setLong(1, id);
            LOG.info("Executing statement '{}'", stmt);

            // Execute the statement
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                LOG.error("Failed to delete entity with ID {}", id);
                throw new EntityNotFoundException();
            }
            LOG.info("Deleted entity with ID {}", id);
        } catch (SQLException e) {
            LOG.error("SQL Exception when deleting entity with ID {}", id, e);
            throw new RepositoryException("Database error during entity deletion", e);
        }
        return entity;
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
}
