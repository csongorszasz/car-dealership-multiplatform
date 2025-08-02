package edu.bbte.idde.scim2304.backend.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DAO-specific configuration bean.
 */
public class DaoConfiguration {
    @JsonProperty("factoryClass")
    private String daoFactoryClassPath;

    public String getDaoFactoryClassPath() {
        return daoFactoryClassPath;
    }

    public void setDaoFactoryClassPath(String daoFactoryClassPath) {
        this.daoFactoryClassPath = daoFactoryClassPath;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DaoConfiguration{");
        sb.append("daoFactoryClassPath='").append(daoFactoryClassPath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
