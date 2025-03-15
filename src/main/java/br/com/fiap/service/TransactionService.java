package br.com.fiap.service;

import br.com.fiap.model.*;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionService {
    public void addIncome(Account account, LocalDate date, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("O valor do recebimento deve ser positivo.");

        Transaction transaction = new Income(UUID.randomUUID().toString(), amount, date, "Depósito");
        account.addTransaction(transaction);
    }

    public void addExpense(Account account, LocalDate date, double amount, ExpenseCategory category) {
        if (amount <= 0) throw new IllegalArgumentException("O valor da despesa deve ser positivo.");

        if (account.getBalance() < amount) throw new IllegalArgumentException("Saldo insuficiente.");

        Transaction transaction = new Expense(UUID.randomUUID().toString(), -amount, date, "Gasto", category);
        account.addTransaction(transaction);
    }

    public void addTransfer(Account from, Account to, LocalDate date, double amount) {
        if (from.getBalance() < amount) throw new IllegalArgumentException("Saldo insuficiente para transferência.");
        if (from.equals(to)) throw new IllegalArgumentException("Não é possível transferir para a mesma conta.");
        if (amount <= 0) throw new IllegalArgumentException("O valor da transferência deve ser positivo.");

        from.addTransaction(new Transfer(UUID.randomUUID().toString(), -amount, date, "Transferência para " + to.getName(), TransactionType.TRANSFER, from, to));
        to.addTransaction(new Transfer(UUID.randomUUID().toString(), amount, date, "Transferência de " + from.getName(), TransactionType.TRANSFER, from, to));
    }
}
