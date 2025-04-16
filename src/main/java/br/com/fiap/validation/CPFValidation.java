package br.com.fiap.validation;

import br.com.fiap.dao.UserDao;
import br.com.fiap.exceptions.CpfAlreadyExistsException;
import br.com.fiap.model.User;

public class CPFValidation implements UserValidation{
    private final UserDao userDao = new UserDao();
    @Override
    public void validate(User user) {
        if(userDao.findByCPF(user.getCpf())){
            throw new CpfAlreadyExistsException(user.getCpf());
        }
    }
}
