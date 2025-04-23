package br.com.fiap.exceptions;

public class AccountNameAlreadyExistsException extends RuntimeException {
  public AccountNameAlreadyExistsException(String name) {
    super("Já existe uma conta cadastrada com o nome: " + name);
  }
}
