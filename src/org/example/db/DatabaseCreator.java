package org.example.db;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

/**
 * Пытается создать БД приложения (например {@code studs}), подключившись к {@code postgres}.
 * На helios часто нужен {@code createdb studs}; если есть права CREATEDB, это сработает из Java.
 */
public final class DatabaseCreator {

    private DatabaseCreator() {
    }

    public static void tryCreateApplicationDatabase(Dotenv dotenv) {
        if (!Boolean.parseBoolean(AppConfig.get(dotenv, "AUTO_CREATE_DATABASE", "false"))) {
            return;
        }
        String host = AppConfig.get(dotenv, "PG_HOST", "pg");
        String port = AppConfig.get(dotenv, "PG_PORT", "5432");
        String database = AppConfig.get(dotenv, "PG_DATABASE", "studs");
        if (!isSafeIdent(database)) {
            System.err.println("AUTO_CREATE_DATABASE: unsafe database name, skip: " + database);
            return;
        }

        String user = AppConfig.firstNonBlank(
                AppConfig.get(dotenv, "PG_USER", ""),
                System.getenv("PG_USER"),
                System.getenv("USER")
        );
        if (user == null || user.isBlank()) {
            System.err.println("AUTO_CREATE_DATABASE: PG_USER / USER not set, skip");
            return;
        }
        String password = AppConfig.firstNonBlank(
                AppConfig.get(dotenv, "PG_PASSWORD", ""),
                System.getenv("PG_PASSWORD"),
                System.getenv("PGPASSWORD")
        );
        if (password == null || password.isBlank()) {
            password = user;
        }

        String adminUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";
        try (Connection c = DriverManager.getConnection(adminUrl, user, password);
             Statement st = c.createStatement()) {
            String checkSql = "SELECT 1 FROM pg_database WHERE datname = '" + database.replace("'", "''") + "'";
            try (ResultSet rs = st.executeQuery(checkSql)) {
                if (rs.next()) {
                    return;
                }
            }
            st.executeUpdate("CREATE DATABASE " + quoteIdent(database));
            System.out.println("Created database: " + database);
        } catch (Exception e) {
            System.err.println("AUTO_CREATE_DATABASE: " + e.getMessage());
            System.err.println("Create DB manually on helios: createdb -h " + host + " " + database);
        }
    }

    private static boolean isSafeIdent(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return name.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    private static String quoteIdent(String ident) {
        return "\"" + ident.replace("\"", "\"\"") + "\"";
    }
}
