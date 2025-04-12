package br.com.fiap.utils;

import br.com.fiap.factory.ConnectionFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class MigrationRunner {
    public static void runMigrations() {
        try (Connection conn = ConnectionFactory.getConnection()) {
            createMigrationsTableIfNotExist(conn);
            Set<String> applied = getAppliedMigrations(conn);
            Path path = Paths.get(ClassLoader.getSystemResource("migrations").toURI());
            Files.list(path)
                    .filter(p -> p.toString().endsWith(".sql"))
                    .sorted()
                    .forEach(file -> {
                        String filename = file.getFileName().toString();
                        if (!applied.contains(filename)) {
                            System.out.println("Rodando migration: " + filename);
                            try {
                                String sql = Files.readString(file);
                                try (Statement stmt = conn.createStatement()) {
                                    stmt.execute(sql);
                                }

                                saveMigrations(conn, filename);
                            } catch (Exception e) {
                                System.out.println("Erro ao aplicar a migration: " + filename + ": " + e.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createMigrationsTableIfNotExist(Connection conn) throws SQLException {
        String sql = "BEGIN " +
                "EXECUTE IMMEDIATE 'CREATE TABLE MIGRATIONS (filename VARCHAR2(255) PRIMARY KEY, applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)'; " +
                "EXCEPTION WHEN OTHERS THEN IF SQLCODE != -955 THEN RAISE; END IF; " +
                "END;";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static Set<String> getAppliedMigrations(Connection conn) throws SQLException {
        Set<String> applied = new HashSet<>();
        String sql = "SELECT filename FROM MIGRATIONS";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                applied.add(rs.getString("filename"));
            }
        }

        return applied;
    }

    private static void saveMigrations(Connection conn, String filename) throws SQLException {
        String sql = "INSERT INTO MIGRATIONS (filename) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, filename);
            stmt.executeUpdate();
        }
    }
}
