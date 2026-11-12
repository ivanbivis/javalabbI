package ru.bivis.car.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;


public class Database implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(Database.class);

    private final HikariDataSource dataSource;
    private final String schema;

    public Database(DatabaseProperties properties) {
        this.schema = properties.getSchema();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUser());
        config.setPassword(properties.getPassword());
        config.setSchema(schema);
        config.setMaximumPoolSize(5);
        this.dataSource = new HikariDataSource(config);

        log.info("Подключение к {} пользователем {}, схема {}", properties.getUrl(), properties.getUser(), schema);
    }

    /** Создаёт таблицу car */
    public void migrate() {
        Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .defaultSchema(schema)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
