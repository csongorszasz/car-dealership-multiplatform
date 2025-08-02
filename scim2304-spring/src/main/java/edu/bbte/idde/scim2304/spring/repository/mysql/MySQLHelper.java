package edu.bbte.idde.scim2304.spring.repository.mysql;

import edu.bbte.idde.scim2304.spring.config.DataSourceConfiguration;
import edu.bbte.idde.scim2304.spring.model.BaseEntity;
import edu.bbte.idde.scim2304.spring.repository.exceptions.RepositoryException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper class containing utility methods for MySQL DAOs.
 */
@Component
@Profile("jdbc")
public class MySQLHelper {
    // Map Java types to SQL types
    private final Map<Class<?>, String> typeToSqlType;

    public MySQLHelper() {
        typeToSqlType = new ConcurrentHashMap<>();
        typeToSqlType.put(Integer.class, "int");
        typeToSqlType.put(int.class, "int");
        typeToSqlType.put(Long.class, "bigint");
        typeToSqlType.put(Float.class, "float");
        typeToSqlType.put(float.class, "float");
        typeToSqlType.put(String.class, "varchar(255)");
        typeToSqlType.put(Date.class, "datetime");
    }

    /**
      * Helper method to build SQL INSERT command with all columns selected except for the ID.
     */
    <T extends BaseEntity> String buildInsertSql(MySQLDao<T> mySQLDao) {
        StringBuilder inserter = new StringBuilder("INSERT INTO ")
            .append(mySQLDao.getModelClassTypeName())
            .append(" (");
        for (var field : mySQLDao.fields) {
            inserter.append(field.getName()).append(", ");
        }
        inserter.delete(inserter.length() - 2, inserter.length()); // Remove trailing comma
        inserter.append(") VALUES (")
            .append("?, ".repeat(mySQLDao.fields.size()))
            .delete(inserter.length() - 2, inserter.length())  // Remove last comma
            .append(')');
        return inserter.toString();
    }

    /**
      * Helper method to capitalize the first letter of a field name.
      */
    static String capitalizeFirstLetter(String name) {
        return name.substring(0, 1).toUpperCase(Locale.getDefault()) + name.substring(1);
    }

    /**
     * Helper method to find a field by its name.
     */
    protected <T extends BaseEntity> Field getFieldByName(MySQLDao<T> mySQLDao, String fieldName) {
        for (var field : mySQLDao.fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Initialize the table for the entity.
     * ! Must be called before any other operations, preferably right after instantiation.
     */
    public <T extends BaseEntity> void initializeTable(MySQLDao<T> mySQLDao, ConnectionPool connectionPool,
                                                              DataSourceConfiguration mySqlConfiguration)
            throws RepositoryException {
        // Create table if it's given in the configuration
        if (mySqlConfiguration.getCreateTables()) {
            createTable(mySQLDao, connectionPool);
        }
    }

    <T extends BaseEntity> void createTable(MySQLDao<T> mySQLDao, ConnectionPool connectionPool)
            throws RepositoryException {
        try (var conn = connectionPool.getConnection();
             var stmt = conn.createStatement()) {
            MySQLDao.LOG.info("Initializing table for {}", mySQLDao.getModelClassTypeName());

            // Build the "CREATE TABLE" SQL command
            StringBuilder command = new StringBuilder();
            command.append("CREATE TABLE IF NOT EXISTS ").append(mySQLDao.getModelClassTypeName()).append(" (");
            command.append("id bigint primary key auto_increment, ");
            for (var field : mySQLDao.fields) {
                command.append(field.getName())
                    .append(' ')
                    .append(typeToSqlType.get(field.getType()))
                    .append(", ");
            }
            command.delete(command.length() - 2, command.length());  // Remove the last comma and space
            command.append(')');
            MySQLDao.LOG.info("Executing command: {}", command);
            stmt.executeUpdate(command.toString());
            // Check if table did not exist already and a new table was created
            if (stmt.getUpdateCount() == 0) {
                MySQLDao.LOG.info("Table '{}' already exists, skipping creation", mySQLDao.getModelClassTypeName());
            } else {
                MySQLDao.LOG.info("Table created successfully");
            }
        } catch (SQLException e) {
            MySQLDao.LOG.error("Error creating table", e);
            throw new RepositoryException("Error creating table", e);
        }
    }
}
