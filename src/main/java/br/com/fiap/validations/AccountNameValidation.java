package br.com.fiap.validations;

import br.com.fiap.dao.AccountDao;
import br.com.fiap.exceptions.AccountNameAlreadyExistsException;
import br.com.fiap.model.Account;


public class AccountNameValidation implements AccountValidation{
    private final AccountDao accountDao = new AccountDao();

    @Override
    public void validate (Account account){
        if(accountDao.existsByName(account.getName())){
            throw new AccountNameAlreadyExistsException(account.getName());
        }
    }
}
