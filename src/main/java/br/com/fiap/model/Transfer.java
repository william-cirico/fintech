package br.com.fiap.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transfer extends Transaction {
    private final Account from;
    private final Account to;

    public Transfer(Long id, double amount, LocalDate date, String description, TransactionType type, Account from, Account to) {
        super(id, amount, date, description, type);
        this.from = from;
        this.to = to;
    }

    public Account getFrom() {
        return from;
    }

    public Account getTo() {
        return to;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("[%s] %s - %s (%.2f) - De: %s | Para: %s",
                this.getDate().format(formatter),
                "🔄 Transferência",
                this.getDescription(),
                this.getAmount(),
                this.getFrom().getName(),
                this.getTo().getName());
    }
}
