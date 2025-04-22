package br.com.fiap.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transfer extends Transaction {
    private final Account destinationAccount;
    private final Long destinationAccountId;

    public Transfer(Long id, double amount, LocalDate date, String description, String observations,  LocalDateTime createdAt, Account originAccount, Account destinationAccount) {
        super(id, amount, date, description, observations, createdAt, TransactionType.TRANSFER, originAccount.getId());
        this.destinationAccount = destinationAccount;
        this.destinationAccountId = destinationAccount.getId();
    }

    public Transfer(Long id, double amount, LocalDate date, String description, String observations, LocalDateTime createdAt, Long originAccountId, Long destinationAccountId) {
        super(id, amount, date, description, observations, createdAt, TransactionType.TRANSFER, originAccountId);
        this.destinationAccount = null;
        this.destinationAccountId = destinationAccountId;
    }

    public Account getDestinationAccount() {return destinationAccount;}

    public Long getDestinationAccountId() {return destinationAccountId;}

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("[%s] %s - %s (%.2f) - De: %s | Para: %s",
                this.getDate().format(formatter),
                "🔄 Transferência",
                this.getDescription(),
                this.getAmount(),
                this.getOriginAccount().getName(),
                this.getDestinationAccount().getName());
    }
}
