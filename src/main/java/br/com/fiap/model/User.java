package br.com.fiap.model;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private final String id;
    private String name;
    private String cpf;
    private String username;
    private final String password;
    private final List<Account> accounts = new ArrayList<>();

    public User(String id, String name, String cpf, String username, String password) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.username = username;
        this.password = password;
    }

    public String getId() {
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

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }
}
