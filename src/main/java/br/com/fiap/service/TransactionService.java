package br.com.fiap.service;

import br.com.fiap.model.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Classe responsável por gerenciar transações financeiras em contas bancárias.
 */
public class TransactionService {

    /**
     * Adiciona um valor como receita a uma conta.
     *
     * @param account Conta que receberá o crédito.
     * @param date Data da transação.
     * @param amount Valor a ser depositado (deve ser positivo).
     */
    public void addIncome(Account account, LocalDate date, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("O valor do recebimento deve ser positivo.");

        // Criação da transação de receita
        Transaction transaction = new Income(UUID.randomUUID().toString(), amount, date, "Depósito");

        // Adiciona a transação na conta e incrementa o saldo
        account.addTransaction(transaction);
        account.deposit(amount);
    }

    /**
     * Adiciona um valor como despesa a uma conta.
     *
     * @param account Conta da qual será debitado o valor.
     * @param date Data da transação.
     * @param amount Valor da despesa (deve ser positivo).
     * @param category Categoria da despesa.
     */
    public void addExpense(Account account, LocalDate date, double amount, ExpenseCategory category) {
        if (amount <= 0)
            throw new IllegalArgumentException("O valor da despesa deve ser positivo.");

        if (account.getBalance() < amount)
            throw new IllegalArgumentException("Saldo insuficiente.");

        // Criação da transação de despesa (valor negativo)
        Transaction transaction = new Expense(UUID.randomUUID().toString(), -amount, date, "Gasto", category);

        // Adiciona a transação na conta e reduz o saldo
        account.addTransaction(transaction);
        account.withdraw(amount);
    }

    /**
     * Realiza uma transferência entre duas contas.
     *
     * @param from Conta de origem da transferência.
     * @param to Conta de destino da transferência.
     * @param date Data da transação.
     * @param amount Valor da transferência (deve ser positivo).
     */
    public void addTransfer(Account from, Account to, LocalDate date, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");

        if (from.equals(to))
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta.");

        if (from.getBalance() < amount)
            throw new IllegalArgumentException("Saldo insuficiente para transferência.");

        // Criação das transações de transferência
        from.addTransaction(new Transfer(
                UUID.randomUUID().toString(),
                -amount,
                date,
                "Transferência para " + to.getName(),
                TransactionType.TRANSFER,
                from,
                to
        ));

        to.addTransaction(new Transfer(
                UUID.randomUUID().toString(),
                amount,
                date,
                "Transferência de " + from.getName(),
                TransactionType.TRANSFER,
                from,
                to
        ));

        // Realiza a movimentação do saldo entre as contas
        from.withdraw(amount);
        to.deposit(amount);
    }
}
