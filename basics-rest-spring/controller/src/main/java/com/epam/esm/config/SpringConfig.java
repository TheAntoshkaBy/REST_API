package com.epam.esm.config;

import com.epam.esm.dao.Impl.CertificateDAOJDBCTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@PropertySource("classpath:path.properties")
public class SpringConfig {

    private final ApplicationContext applicationContext;
    @Value("${path.database}")
    private String propertiesPath;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig(propertiesPath);
        return new HikariDataSource(config);
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    @Bean
    public CertificateDAOJDBCTemplate certificateDAOJDBCTemplate(){
        return new CertificateDAOJDBCTemplate(jdbcTemplate());
    }


}