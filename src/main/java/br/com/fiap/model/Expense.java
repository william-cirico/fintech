package br.com.fiap.model;

import java.time.LocalDate;

public class Expense extends Transaction {
    private ExpenseCategory category;


    public Expense(double amount, LocalDate date, String description, ExpenseCategory category) {
        super(amount, date, description, TransactionType.EXPENSE);
        this.category = category;
    }

    public Expense(double amount, LocalDate date, ExpenseCategory category) {
        super(amount, date, TransactionType.EXPENSE);
        this.category = category;
    }
}

