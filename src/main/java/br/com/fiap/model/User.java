package br.com.fiap.model;

import br.com.fiap.service.LoginService;

import java.rmi.UnexpectedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private final int id;
    private String name;
    private String cpf;
    private String username;
    private String password;
    private final LoginService login = new LoginService();
    private final List<Account> accounts = new ArrayList<>();

    public User(int id, String name, String cpf, LocalDate birthdate, String username, String password) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.username = username;
        this.password = this.login.generatePasswordHash(password);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) throws UnexpectedException {
        this.cpf = cpf;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void changePassword(String password) {
        this.password = this.login.generatePasswordHash(password);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }
}
