package br.com.fiap.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Income extends Transaction {
    public Income(Long id, double amount, LocalDate date, String description, String observations, LocalDateTime createdAt, Long originAccountId) {
        super(id, amount, date, description, observations, createdAt, TransactionType.INCOME, originAccountId);
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
