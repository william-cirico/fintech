package br.com.fiap.model;

import java.time.LocalDate;

public class Transfer extends Transaction {
    private final Account from;
    private final Account to;

    public Transfer(String id, double amount, LocalDate date, String description, TransactionType type, Account from, Account to) {
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
}
