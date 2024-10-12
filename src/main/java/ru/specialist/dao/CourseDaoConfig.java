package ru.specialist.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("jdbc.properties")
@ComponentScan("ru.specialist.dao")
public class CourseDaoConfig {
    @Autowired //Аннотация @Autowired в Spring используется для автоматического внедрения зависимостей
    private Environment env;

    @Bean
    public DataSource webDataSource() {


        // DriverManagerDataSource - всегда возвращает новое соединение
        // SingleConnectionDataSource - всегда возвращает одно и то же соединение
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        ds.setUrl(env.getRequiredProperty("jdbc.url"));
        ds.setUsername(env.getRequiredProperty("jdbc.username"));
        ds.setPassword(env.getRequiredProperty("jdbc.password"));

        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(webDataSource());
    } // зависимость, это нужно для реализации нашего репозитория

}
