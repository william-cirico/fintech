package br.com.fiap.exceptions;

public class OptionalAccountInvalidException extends RuntimeException {
    public OptionalAccountInvalidException(int option) {
        super("A opção " + option + " não existe!");
    }
}
