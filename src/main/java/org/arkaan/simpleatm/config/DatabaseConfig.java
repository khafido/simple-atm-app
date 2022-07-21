package org.arkaan.simpleatm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseConfig {

    @Value("#{environment.DATASOURCE_URL}")
    private String dbUrl;

    @Value("#{environment.DATASOURCE_USER}")
    private String dbUser;

    @Value("#{environment.DATASOURCE_PASSWORD}")
    private String dbPassword;

    @Bean
    public EntityManager getEntityManagerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.jdbc.url", dbUrl);
        props.put("javax.persistence.jdbc.user", dbUser);
        props.put("javax.persistence.jdbc.password", dbPassword);
        return Persistence.createEntityManagerFactory("atm", props)
                .createEntityManager();
    }
}
