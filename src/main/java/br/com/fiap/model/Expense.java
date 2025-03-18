package br.com.fiap.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense extends Transaction {
    private ExpenseCategory category;

    public Expense(String id, double amount, LocalDate date, String description, ExpenseCategory category) {
        super(id, amount, date, description, TransactionType.EXPENSE);
        this.category = category;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("[%s] %s - %s (%.2f) - Categoria: %s",
                this.getDate().format(formatter),
                "🛒 Despesa",
                this.getDescription(),
                this.getAmount(),
                category.getName());
    }
}

