package br.com.fiap.validation;

import br.com.fiap.exceptions.PasswordsDoNotMatchException;
import br.com.fiap.model.User;

public class PasswordMatchValidation implements UserValidation {
    private String password;
    private String confirmPassword;

    public PasswordMatchValidation(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @Override
    public void validate(User user) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordsDoNotMatchException("As senhas não são iguais");
        }
    }
}
