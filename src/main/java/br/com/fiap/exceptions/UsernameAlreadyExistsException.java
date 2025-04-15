package br.com.fiap.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("O username " + username + " já está em uso.");
    }
}
