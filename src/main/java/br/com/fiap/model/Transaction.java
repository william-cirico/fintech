package br.com.fiap.model;

import java.time.LocalDate;

public abstract class Transaction {
    private double amount;
    private LocalDate date;
    private String description;
    private String observations;
    private final TransactionType type;

    public Transaction(double amount, LocalDate date, String description, TransactionType type) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.type = type;
    }

    public Transaction(double amount, LocalDate date, TransactionType type) {
        this(amount, date, "", type);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public TransactionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Tipo da Transação: " + getType() + ", \n" +
                "Valor: " + amount + ", \n" +
                "Data: " + date + ", \n" +
                "Descrição: '" + description;
    }
}
