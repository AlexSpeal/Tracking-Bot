/*
package edu.java.configuration;


import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        var values = applicationConfig.dataSourceValues();

        dataSource.setDriverClassName(values.driverClassName());
        dataSource.setUrl(values.url());
        dataSource.setUsername(values.username());
        dataSource.setPassword(values.password());

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
*/
