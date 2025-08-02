package edu.bbte.idde.scim2304.web.om;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectMapperFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectMapperFactory.class);

    private static ObjectMapper objectMapper;

    public static synchronized ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            LOG.info("Initializing ObjectMapper");
            objectMapper = new ObjectMapper();
        }

        return objectMapper;
    }
}
