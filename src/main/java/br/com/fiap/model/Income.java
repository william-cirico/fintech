package br.com.fiap.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Income extends Transaction {
    public Income(Long id, double amount, LocalDate date, String description) {
        super(id, amount, date, description, TransactionType.INCOME);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("[%s] %s - %s (%.2f)",
                this.getDate().format(formatter),
                "💰 Receita",
                this.getDescription(),
                this.getAmount());
    }
}
