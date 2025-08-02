package edu.bbte.idde.scim2304.backend.repository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a DAO which uses database connectivity.
 */
public interface JdbcDao {
    Connection getConnection() throws SQLException;

    void evictConnection(Connection connection);
}
