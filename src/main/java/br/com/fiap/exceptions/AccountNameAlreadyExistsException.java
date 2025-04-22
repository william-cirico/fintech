package br.com.fiap.exceptions;

public class AccountNameAlreadyExistsException extends RuntimeException {
  public AccountNameAlreadyExistsException(int option) {
    super("A opção " + option + " não foi cadastrada.");
  }
}
