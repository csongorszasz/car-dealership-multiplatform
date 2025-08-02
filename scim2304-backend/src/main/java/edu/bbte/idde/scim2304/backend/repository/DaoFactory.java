package edu.bbte.idde.scim2304.backend.repository;

import edu.bbte.idde.scim2304.backend.config.ConfigurationFactory;
import edu.bbte.idde.scim2304.backend.repository.exceptions.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic DAO factory.
 */
public abstract class DaoFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DaoFactory.class);
    private static DaoFactory instance;

    /**
     * The data access method is decided on the first call of this method.
     */
    public static synchronized DaoFactory getInstance() {
        if (instance == null) {
            // load dao factory class by using class path from configuration
            try {
                String daoFactoryClassPath = ConfigurationFactory.getDaoConfiguration().getDaoFactoryClassPath();
                LOG.info("Attempting to load DAO factory class: {}", daoFactoryClassPath);
                instance = (DaoFactory) Class.forName(daoFactoryClassPath).getDeclaredConstructor().newInstance();
                LOG.info("DAO factory class loaded successfully");
            } catch (ReflectiveOperationException e) {
                LOG.error("Failed to load DAO factory class", e);
                throw new RepositoryException("Failed to load DAO factory class", e);
            }
        }
        return instance;
    }

    public abstract AdvertDao getAdvertDao();
}
