package br.com.fiap.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final Long id;
    private String name;
    private double balance;
    private List<Transaction> transactions = new ArrayList<>();
    private List<Investment> investments = new ArrayList<>();



    public Long getId() {
        return id;
    }

    public List<Investment> getInvestments() {
        return investments;
    }

    public String getName() {
        return name;
    }

    public Account(Long id, String name, double balance) {
        this.id = id;
        this.balance = balance;
        this.name = name;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void addInvestment(Investment investimento) {
        if (investimento.getAmount() > balance) {
            return;
        }
        investments.add(investimento);
        System.out.println("Investimento realizado com sucesso!");
    }


    public boolean withdraw(double value) {
        if (balance < value || value <= 0) {
            return false;
        }

        balance -= value;
        return true;
    }


    public void deposit(double value) {
        if(balance <= 0) throw new IllegalArgumentException("Valor do saque não pode ser negativo ou igual a zero.");

        balance += value;
    }



    public void showReport() {

    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

