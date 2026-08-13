package bank.config;



import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {

        String url = DOTENV.get("DB_URL");
        String username = DOTENV.get("DB_USERNAME");
        String password = DOTENV.get("DB_PASSWORD");

        if (url == null || username == null || password == null) {
            throw new IllegalStateException(
                    "Database environment variables are not properly configured."
            );
        }

        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }
}