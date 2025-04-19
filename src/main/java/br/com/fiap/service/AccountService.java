package br.com.fiap.service;

import br.com.fiap.dao.AccountDao;

import br.com.fiap.exceptions.AccountNameAlreadyExistsException;
import br.com.fiap.exceptions.OptionalAccountInvalidException;
import br.com.fiap.model.Account;

import br.com.fiap.model.User;
import br.com.fiap.validation.AccountNameValidation;

import java.util.List;


public class AccountService {
    AccountDao accountDao = new AccountDao();
    AccountNameValidation accountNameValidation = new AccountNameValidation();

    public Account addAccount(Account account) {
        accountNameValidation.validate(account);
        accountDao.insert(account);
        return account;
    }

    public List<Account> findUserAccounts(User user) {
        return accountDao.findAllById(user.getId());
    }
}



