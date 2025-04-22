package br.com.fiap.validations;

import br.com.fiap.dao.UserDao;
import br.com.fiap.exceptions.CpfAlreadyExistsException;
import br.com.fiap.model.User;

public class CPFValidation implements UserValidation{
    private final UserDao userDao;

    public CPFValidation(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void validate(User user) {

        if(userDao.existsByCPF(user.getCpf())){
            throw new CpfAlreadyExistsException(user.getCpf());
        }
    }
}
