package edu.bbte.idde.scim2304.backend.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic configuration bean for the application.
 * Contains other module-specific configuration beans.
 */
public class MainConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(MainConfiguration.class);

    @JsonProperty("dev")
    private ProfileConfiguration devProfileConfiguration = new ProfileConfiguration();

    @JsonProperty("prod")
    private ProfileConfiguration prodProfileConfiguration = new ProfileConfiguration();

    public ProfileConfiguration getDevProfileConfiguration() {
        return devProfileConfiguration;
    }

    public void setDevProfileConfiguration(ProfileConfiguration devProfileConfiguration) {
        this.devProfileConfiguration = devProfileConfiguration;
    }

    public ProfileConfiguration getProdProfileConfiguration() {
        return prodProfileConfiguration;
    }

    public void setProdProfileConfiguration(ProfileConfiguration prodProfileConfiguration) {
        this.prodProfileConfiguration = prodProfileConfiguration;
    }

    public ProfileConfiguration getProfileConfiguration() {
        String profile = System.getenv("PROFILE");
        if (profile == null) {
            throw new IllegalArgumentException("PROFILE environment variable not set");
        }
        LOG.info("Using profile: {}", profile);
        if ("dev".equals(profile)) {
            return devProfileConfiguration;
        } else if ("prod".equals(profile)) {
            return prodProfileConfiguration;
        } else {
            throw new IllegalArgumentException("Unknown profile: " + profile);
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MainConfiguration{");
        sb.append("devProfileConfiguration=").append(devProfileConfiguration);
        sb.append(", prodProfileConfiguration=").append(prodProfileConfiguration);
        sb.append('}');
        return sb.toString();
    }
}
