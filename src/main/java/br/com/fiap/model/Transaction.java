package br.com.fiap.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Transaction {
    private Long id;
    private double amount;
    private LocalDate date;
    private String description;
    private String observations;
    private LocalDateTime createdAt;
    private final TransactionType type;
    private final Long originAccountId;
    private final Account originAccount;



    public Transaction(Long id, double amount, LocalDate date, String description, String observations, LocalDateTime createdAt, TransactionType type, Long originAccountId) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.observations = observations;
        this.createdAt = createdAt;
        this.type = type;
        this.originAccountId = originAccountId;
        this.originAccount = null;
    }

    public Transaction(Long id, double amount, LocalDate date, String description, String observations, LocalDateTime createdAt, TransactionType type, Account originAccount) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.observations = observations;
        this.createdAt = createdAt;
        this.type = type;
        this.originAccount = originAccount;
        this.originAccountId = originAccount.getId();
    }

    public Account getOriginAccount() {return originAccount;}

    public Long getOriginAccountId() { return originAccountId; };

    public Long getId() {
        return id;
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
                "Descrição: " + description;
    }
}
