package br.com.fiap.model;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String name;
    private String cpf;
    private String username;
    private String password;
    private final List<Account> accounts = new ArrayList<>();
    private String createdAt;

    public User(Long id,String name, String cpf, String username, String password, String createdAt) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }

    public User(String name, String cpf, String username, String password) {
        this.name = name;
        this.cpf = cpf;
        this.username = username;
        this.password = password;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

   public void setPassword(String password) { this.password = password;}



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
