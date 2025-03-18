package br.com.fiap.service;

import br.com.fiap.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
    private final TransactionService transactionService = new TransactionService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public double getTotalExpensesByCategoryTypeAndPeriodFromAccount(Account account, ExpenseCategoryType type, LocalDate start, LocalDate end) {
        return transactionService.getExpensesByCategoryTypeAndPeriodFromAccount(account, type, start, end).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalIncomeByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        return transactionService.getTransactionsByTypeAndPeriodFromAccount(account, TransactionType.INCOME, start, end)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public void printIncomeReportByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Transaction> income = transactionService.getTransactionsByTypeAndPeriodFromAccount(account, TransactionType.INCOME, start, end);
        System.out.println("\n📊 Relatório de Receitas (" + start.format(formatter) + " - " + end.format(formatter) + "):");
        for (Transaction t : income) {
            System.out.println(t);
        }

        double total = getTotalIncomeByPeriodFromAccount(account, start, end);

        System.out.println("Total em Receitas: " + total);
    }

    public void printExpenseReportByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Transaction> expense = transactionService.getTransactionsByTypeAndPeriodFromAccount(account, TransactionType.EXPENSE, start, end);
        System.out.println("\n📊 Relatório de Despesas (" + start.format(formatter) + " - " + end.format(formatter) + "):");
        for (Transaction t : expense) {
            System.out.println(t);
        }

        double total = getTotalExpensesByPeriodFromAccount(account, start, end);
        System.out.println("Total em Despesas: " + total);
    }

    public void printTransferReportByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Transaction> transfer = transactionService.getTransactionsByTypeAndPeriodFromAccount(account, TransactionType.TRANSFER, start, end);
        System.out.println("\n📊 Relatório de Transferências (" + start.format(formatter) + " - " + end.format(formatter) + "):");
        for (Transaction t : transfer) {
            System.out.println(t);
        }
    }

    public double getTotalExpensesByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        return transactionService.getTransactionsByTypeAndPeriodFromAccount(account, TransactionType.EXPENSE, start, end)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Map<ExpenseCategory, Double> getTopExpenseCategoriesByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        return transactionService.getTransactionsByTypeAndPeriodFromAccount(account, TransactionType.EXPENSE, start, end)
                .stream()
                .map(t -> (Expense) t)
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<ExpenseCategory, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public void printTotalExpensesByCategoryTypeAndPeriodFromAccount(Account account, ExpenseCategoryType type, LocalDate start, LocalDate end) {
        double totalExpenses = getTotalExpensesByCategoryTypeAndPeriodFromAccount(account, type, start, end);
        System.out.println("\n📊 Total de despesas " + type.toString().toLowerCase() + "(" + start.format(formatter) + " - " + end.format(formatter) + "): " + totalExpenses);
    }

    public void printTopExpenseCategoriesByPeriod(Account account, LocalDate start, LocalDate end) {
        Map<ExpenseCategory, Double> topCategories = getTopExpenseCategoriesByPeriodFromAccount(account, start, end);

        System.out.println("Relatório de Despesas por Categoria");
        System.out.println("Período: " + start + " a " + end);
        System.out.println("--------------------------------------");

        if (topCategories.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada nesse período.");
        } else {
            topCategories.forEach((category, total) ->
                    System.out.printf("%-20s R$ %.2f%n", category.getName(), total)
            );
        }

        System.out.println("--------------------------------------");
    }

    public void printAccountReport(Account account) {
        List<Transaction> expenses = account.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE) // Filtra todas as transações que são instancias da classe Expense
                .toList();
        List<Transaction> incomes = account.getTransactions().stream().filter(t -> t.getType() == TransactionType.INCOME)
                .toList();

        // Exibe os gastos
        System.out.println("\nTransações: ");
        System.out.println("===============================================================");
        System.out.println("\nGastos: ");
        for (Transaction expense : expenses) {
            System.out.println(expense);
        }
        System.out.println("===============================================================");

        // Exibe os recebimentos
        System.out.println("\nRecebimentos: ");
        for (Transaction income : incomes) {
            System.out.println(income);
        }
        System.out.println("===============================================================");

        // Exibe investimentos
        System.out.println("\nInvestimentos: ");
        for (Investment investimento : account.getInvestments()) {
            System.out.println(investimento);
        }

        // Saldo
        System.out.println("Saldo da conta: " + account.getBalance());
    }
}
