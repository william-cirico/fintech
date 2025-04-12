package br.com.fiap.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(Throwable cause) {
        super("Ocorreu um erro no banco de dados: " + cause);
    }
}
