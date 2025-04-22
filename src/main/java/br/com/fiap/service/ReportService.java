package br.com.fiap.service;

import br.com.fiap.dao.ExpenseDao;
import br.com.fiap.dao.IncomeDao;
import br.com.fiap.dao.InvestmentDao;
import br.com.fiap.dao.TransferDao;
import br.com.fiap.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportService {
    private final ExpenseDao expenseDao = new ExpenseDao();
    private final TransferDao transferDao = new TransferDao();
    private final IncomeDao incomeDao = new IncomeDao();
    private final InvestmentDao investmentDao = new InvestmentDao();

    private final TransactionService transactionService = new TransactionService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public double getTotalExpensesByCategoryTypeAndPeriodFromAccount(Account account, ExpenseCategoryType type, LocalDate start, LocalDate end) {
        return expenseDao.getTotalByPeriodFromAccount(account, start, end);
    }

    public double getTotalIncomeByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        return incomeDao.getTotalByPeriodFromAccount(account, start, end);
    }

    public void printIncomeReportByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Income> income = incomeDao.findAllByPeriodFromAccount(account, start, end);
        System.out.println("\n📊 Relatório de Receitas (" + start.format(formatter) + " - " + end.format(formatter) + "):");
        for (Transaction t : income) {
            System.out.println(t);
        }

        double total = getTotalIncomeByPeriodFromAccount(account, start, end);

        System.out.println("Total em Receitas: " + total);
    }

    public void printExpenseReportByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Expense> expense = expenseDao.findAllByPeriodFromAccount(account, start, end);
        System.out.println("\n📊 Relatório de Despesas (" + start.format(formatter) + " - " + end.format(formatter) + "):");
        for (Transaction t : expense) {
            System.out.println(t);
        }

        double total = getTotalExpensesByPeriodFromAccount(account, start, end);
        System.out.println("Total em Despesas: " + total);
    }

    public void printTransferReportByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Transfer> transfer = transferDao.findAllByPeriodFromAccount(account, start, end);
        System.out.println("\n📊 Relatório de Transferências (" + start.format(formatter) + " - " + end.format(formatter) + "):");
        for (Transaction t : transfer) {
            System.out.println(t);
        }
    }

    public double getTotalExpensesByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        return expenseDao.getTotalByPeriodFromAccount(account, start, end);
    }

    public void printTotalExpensesByCategoryTypeAndPeriodFromAccount(Account account, ExpenseCategoryType type, LocalDate start, LocalDate end) {
        double totalExpenses = getTotalExpensesByCategoryTypeAndPeriodFromAccount(account, type, start, end);
        System.out.println("\n📊 Total de despesas " + type.toString().toLowerCase() + "(" + start.format(formatter) + " - " + end.format(formatter) + "): " + totalExpenses);
    }

    public void printAccountReport(Account account) {
        List<Expense> expenses = expenseDao.findAllByAccountId(account.getId());
        List<Income> incomes = incomeDao.findAllByAccountId(account.getId());

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

        List<Investment> investments = investmentDao.findAllByAccountId(account.getId());

        // Exibe investimentos
        System.out.println("\nInvestimentos: ");
        for (Investment investimento : investments) {
            System.out.println(investimento);
        }

        // Saldo
        System.out.println("Saldo da conta: " + account.getBalance());
    }
}
