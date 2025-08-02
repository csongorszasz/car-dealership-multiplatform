package edu.bbte.idde.scim2304.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Factory class for creating configuration objects.
 * Reads configuration from a JSON file.
 */
public class ConfigurationFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationFactory.class);
    private static final String CONFIG_FILE = "/application.json";

    private static MainConfiguration mainConfiguration = new MainConfiguration();

    // Executed when the class is loaded into memory, before any instance is created or any static method is called
    static {
        // JSON reader initialization
        ObjectMapper objectMapper = new ObjectMapper();

        // Requesting an input stream for the JSON file
        try (InputStream inputStream = ConfigurationFactory.class.getResourceAsStream(CONFIG_FILE)) {
            // Reading the JSON file into an instance of MainConfiguration
            mainConfiguration = objectMapper.readValue(inputStream, MainConfiguration.class);
            LOG.info("Read following configuration: {}", mainConfiguration);
        } catch (IOException e) {
            LOG.error("Error loading configuration", e);
        }
    }

    public static MainConfiguration getMainConfiguration() {
        return mainConfiguration;
    }

    public static JdbcConfiguration getJdbcConfiguration() {
        return mainConfiguration.getProfileConfiguration().getJdbcConfiguration();
    }

    public static ConnectionPoolConfiguration getConnectionPoolConfiguration() {
        return mainConfiguration.getProfileConfiguration().getConnectionPoolConfiguration();
    }

    public static DaoConfiguration getDaoConfiguration() {
        return mainConfiguration.getProfileConfiguration().getDaoConfiguration();
    }
}
