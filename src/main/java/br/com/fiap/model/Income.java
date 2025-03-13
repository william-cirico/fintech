package br.com.fiap.model;

import java.time.LocalDate;

public class Income extends Transaction {
    public Income(String id, double amount, LocalDate date, String description) {
        super(id, amount, date, description, TransactionType.INCOME);
    }
}
