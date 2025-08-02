package edu.bbte.idde.scim2304.spring.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Configuration
@Data
@Profile("!mem")
public class DataSourceConfiguration {
    @Value("${jdbc.createTables}")
    @Getter
    private Boolean createTables;

    @Value("${jdbc.driverClass}")
    private String driverClass;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.poolSize}")
    private Integer poolSize;

    @Bean
    @Lazy
    public HikariDataSource dataSource() {
        var ds = new HikariDataSource();
        ds.setDriverClassName(driverClass);
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setMaximumPoolSize(poolSize);
        return ds;
    }
}
