package br.com.fiap.model;

import java.time.LocalDate;

public class Expense extends Transaction {
    private ExpenseCategory category;

    public Expense(String id, double amount, LocalDate date, String description, ExpenseCategory category) {
        super(id, amount, date, description, TransactionType.EXPENSE);
        this.category = category;
    }
}

