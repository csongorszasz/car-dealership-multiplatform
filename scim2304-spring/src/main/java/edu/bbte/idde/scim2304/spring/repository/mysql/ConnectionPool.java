package edu.bbte.idde.scim2304.spring.repository.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A wrapper for HikariCP to conveniently create and manage connection pools.
 */
@Component
@Profile("jdbc")
public final class ConnectionPool implements Closeable {
    private final HikariDataSource dataSource;

    public ConnectionPool(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Obtains a connection from the connection pool.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Returns a connection to the pool.
     */
    public void evictConnection(Connection connection) {
        dataSource.evictConnection(connection);
    }

    /**
     * Shuts down the data source.
     */
    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}