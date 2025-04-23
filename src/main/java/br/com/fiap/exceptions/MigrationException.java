package br.com.fiap.exceptions;

public class MigrationException extends RuntimeException {
    public MigrationException(String filename, String errorMessage) {
        super("Erro ao aplicar a migration " + filename + ": "  + errorMessage);
    }
}
