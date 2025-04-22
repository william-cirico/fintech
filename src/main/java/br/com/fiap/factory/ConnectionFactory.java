package br.com.fiap.factory;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final Dotenv dotenv = loadDotenv();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASS");

    private static Dotenv loadDotenv() {
        String environment = System.getenv("ENV");
        if (environment == null || environment.isBlank()) {
            environment = "development";
        }

        String envFile = ".env." + environment;

        return Dotenv.configure()
                .filename(envFile)
                .ignoreIfMissing()
                .load();
    }

    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASS == null) {
            throw new SQLException("Você precisa definir as variáveis de ambiente para conexão com o banco de dados");
        }

        return DriverManager.getConnection(URL, USER, PASS);
    }
}
