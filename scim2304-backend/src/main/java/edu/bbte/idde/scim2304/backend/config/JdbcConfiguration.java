package edu.bbte.idde.scim2304.backend.config;

/**
 * JDBC-specific configuration bean.
 */
public class JdbcConfiguration {
    private Boolean createTables = false;  // false by default (for safety)
    private String driverClass;
    private String url;
    private String username;
    private String password;

    public Boolean isCreateTables() {
        return createTables;
    }

    public void setCreateTables(Boolean createTables) {
        this.createTables = createTables;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JdbcConfiguration{");
        sb.append("createTables=").append(createTables);
        sb.append(", driverClass='").append(driverClass).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", username='").append(username).append('\'');
        // password is not printed for security reasons
        sb.append('}');
        return sb.toString();
    }
}
