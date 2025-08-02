package edu.bbte.idde.scim2304.backend.repository.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.bbte.idde.scim2304.backend.config.ConfigurationFactory;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A wrapper for HikariCP to conveniently create and manage connection pools.
 */
public final class ConnectionPool implements Closeable {
    private static ConnectionPool instance;
    private final HikariDataSource dataSource;

    private ConnectionPool(String dbUrl, String username, String password, int maxPoolSize) {
        // Configure HikariCP with provided parameters
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(dbUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maxPoolSize);

        // Initialize the data source with the configured settings
        this.dataSource = new HikariDataSource(config);
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            var mysqlConfiguration = ConfigurationFactory.getJdbcConfiguration();
            var connectionPoolConfiguration = ConfigurationFactory.getConnectionPoolConfiguration();
            instance = new ConnectionPool(
                mysqlConfiguration.getUrl(),
                mysqlConfiguration.getUsername(),
                mysqlConfiguration.getPassword(),
                connectionPoolConfiguration.getPoolSize()
            );
        }
        return instance;
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