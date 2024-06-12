package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSource {
    private static final Logger LOGGER = Logger.getLogger(DataSource.class.getName());
    private static HikariConfig config;
    private static HikariDataSource ds;

    public static void initFromProperties(String propertiesFile) {
        Properties properties = new Properties();
        try {
            properties.load(DataSource.class.getClassLoader().getResourceAsStream(propertiesFile));
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.userName");
            String password = properties.getProperty("db.password");
            init(url, username, password);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "A load exception", e);
        }
    }

    public static void init(String url, String username, String password) {
        config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds = new HikariDataSource( config );
    }



    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
