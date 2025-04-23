package br.com.fiap.factory;

import br.com.fiap.dao.UserDao;
import br.com.fiap.validations.CPFValidation;
import br.com.fiap.validations.PasswordMatchValidation;
import br.com.fiap.validations.UserValidation;
import br.com.fiap.validations.UsernameValidation;

import java.util.ArrayList;
import java.util.List;

public class UserValidationFactory {
    private final UserDao userDao;

    public UserValidationFactory(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<UserValidation> createRegisterUserValidations(String password, String confirmPassword) {
        return new ArrayList<>(List.of(
                new CPFValidation(userDao),
                new UsernameValidation(userDao),
                new PasswordMatchValidation(password, confirmPassword)
        ));
    }
}
