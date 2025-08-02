package edu.bbte.idde.scim2304.backend.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration bean for a profile.
 * It contains JDBC and ConnectionPool configurations.
 */
public class ProfileConfiguration {
    @JsonProperty("jdbc")
    private JdbcConfiguration jdbcConfiguration = new JdbcConfiguration();

    @JsonProperty("connectionPool")
    private ConnectionPoolConfiguration connectionPoolConfiguration = new ConnectionPoolConfiguration();

    @JsonProperty("dao")
    private final DaoConfiguration daoConfiguration = new DaoConfiguration();

    public JdbcConfiguration getJdbcConfiguration() {
        return jdbcConfiguration;
    }

    public void setJdbcConfiguration(JdbcConfiguration jdbcConfiguration) {
        this.jdbcConfiguration = jdbcConfiguration;
    }

    public ConnectionPoolConfiguration getConnectionPoolConfiguration() {
        return connectionPoolConfiguration;
    }

    public void setConnectionPoolConfiguration(ConnectionPoolConfiguration connectionPoolConfiguration) {
        this.connectionPoolConfiguration = connectionPoolConfiguration;
    }

    public DaoConfiguration getDaoConfiguration() {
        return daoConfiguration;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProfileConfiguration{");
        sb.append("jdbcConfiguration=").append(jdbcConfiguration);
        sb.append(", connectionPoolConfiguration=").append(connectionPoolConfiguration);
        sb.append('}');
        return sb.toString();
    }
}
