package org.example.db;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {

    private final String jdbcUrl;
    private final String user;
    private final String password;

    public Database(Dotenv dotenv) {
        String fullUrl = AppConfig.get(dotenv, "PG_JDBC_URL", null);
        if (fullUrl != null && !fullUrl.isBlank()) {
            this.jdbcUrl = fullUrl;
        } else {
            String host = AppConfig.get(dotenv, "PG_HOST", "pg");
            String port = AppConfig.get(dotenv, "PG_PORT", "5432");
            String database = AppConfig.get(dotenv, "PG_DATABASE", "studs");
            this.jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        }

        this.user = AppConfig.firstNonBlank(
                AppConfig.get(dotenv, "PG_USER", ""),
                System.getenv("PG_USER"),
                System.getenv("USER")
        );
        if (this.user == null || this.user.isBlank()) {
            throw new IllegalStateException(
                    "Задайте PG_USER или экспортируйте USER (на helios обычно совпадает с логином).");
        }

        String pwd = AppConfig.firstNonBlank(
                AppConfig.get(dotenv, "PG_PASSWORD", ""),
                System.getenv("PG_PASSWORD"),
                System.getenv("PGPASSWORD")
        );
        if (pwd == null || pwd.isBlank()) {
            pwd = this.user;
        }
        this.password = pwd;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    public String getJdbcUrlForLogging() {
        return jdbcUrl;
    }

    public String getUser() {
        return user;
    }
}
