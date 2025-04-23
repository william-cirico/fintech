package br.com.fiap.validations;

import br.com.fiap.dao.UserDao;
import br.com.fiap.exceptions.UsernameAlreadyExistsException;
import br.com.fiap.model.User;

public class UsernameValidation implements UserValidation {
    private final UserDao userDao;

    public UsernameValidation(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void validate(User user) {
        if (userDao.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
    }
}
