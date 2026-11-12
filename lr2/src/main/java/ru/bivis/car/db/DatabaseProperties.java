package ru.bivis.car.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Параметры подключения к БД.
 * Читаются из файла в ресурсах, переменные окружения DB_URL, DB_USER, DB_PASSWORD, DB_SCHEMA
 */
public class DatabaseProperties {

    private static final Logger log = LoggerFactory.getLogger(DatabaseProperties.class);

    private final String url;
    private final String user;
    private final String password;
    private final String schema;

    public DatabaseProperties(String resourceName) {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                String message = "Файл настроек не найден: " + resourceName;
                log.error(message);
                throw new IllegalStateException(message);
            }
            props.load(in);
        } catch (IOException e) {
            log.error("Не удалось прочитать {}", resourceName, e);
            throw new UncheckedIOException("Не удалось прочитать " + resourceName, e);
        }
        this.url = read(props, "db.url", "DB_URL");
        this.user = read(props, "db.user", "DB_USER");
        this.password = read(props, "db.password", "DB_PASSWORD");
        this.schema = read(props, "db.schema", "DB_SCHEMA");
    }

    private String read(Properties props, String key, String envName) {
        String fromEnv = System.getenv(envName);
        String value = (fromEnv != null && !fromEnv.isBlank()) ? fromEnv : props.getProperty(key);
        if (value == null || value.isBlank()) {
            String message = "Не задан параметр " + key + " (или переменная " + envName + ")";
            log.error(message);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }
}
