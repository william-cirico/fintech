package br.com.fiap.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Account {
    private final String id;
    private String name;
    private double balance;
    private List<Transaction> transactions = new ArrayList<>();
    private List<Investment> investments = new ArrayList<>();



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Account(String id, String name, double balance) {
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
        balance -= investimento.getAmount();
        investments.add(investimento);
        System.out.println("Investimento realizado com sucesso!");
    }


    public boolean withdraw(double value) {
        if (balance < value) {
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
        List<Transaction> expenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE) // Filtra todas as transações que são instancias da classe Expense
                .toList();
        List<Transaction> incomes = transactions.stream().filter(t -> t.getType() == TransactionType.INCOME)
                .toList();

        // Exibe os gastos
        System.out.println("\nTransações: ");
        System.out.println("===============================================================");
        System.out.println("\nGastos: ");
        for (Transaction expense : expenses) {
            System.out.println(expense);
        }
        System.out.println("===============================================================");
        // Exibe os recebimentos
        System.out.println("\nRecebimentos: ");
        for (Transaction income : incomes) {
            System.out.println(income);
        }
        System.out.println("===============================================================");
        // Exibe investimentos
        System.out.println("\nInvestimentos: ");
        for (Investment investimento : investments) {
            System.out.println(investimento);
        }
        // Saldo
        System.out.println("Saldo da conta: " + this.balance);
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

