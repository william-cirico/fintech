package br.com.fiap.exceptions;

public class CpfAlreadyExistsException extends RuntimeException {
    public CpfAlreadyExistsException(String cpf) {
        super("O cpf " + cpf + " já foi utilizado.");
    }
}
