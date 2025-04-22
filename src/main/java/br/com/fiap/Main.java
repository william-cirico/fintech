package br.com.fiap;

import br.com.fiap.view.ViewHandler;
import br.com.fiap.dao.*;
import br.com.fiap.service.*;
import br.com.fiap.utils.MigrationRunner;

public class Main {
    public static void main(String[] args) {
        // Criando serviço de autenticação
        UserDao userDao = new UserDao();
        AuthService authService = new AuthService(userDao);

        // Criando serviço de investimento
        InvestmentDao investmentDao = new InvestmentDao();
        InvestmentService investmentService = new InvestmentService(investmentDao);

        // Criando serviço de transação
        AccountDao accountDao = new AccountDao();
        ExpenseDao expenseDao = new ExpenseDao();
        TransferDao transferDao = new TransferDao();
        IncomeDao incomeDao = new IncomeDao();
        TransactionService transactionService = new TransactionService(incomeDao, accountDao, expenseDao, transferDao);

        // Criando serviço de relatórios
        ReportService reportService = new ReportService(expenseDao, transferDao, incomeDao, investmentDao);
        AccountService accountService = new AccountService();

        // Inicializando View
        ExpenseCategoryDao expenseCategoryDao = new ExpenseCategoryDao();
        ViewHandler viewHandler = new ViewHandler(
                transactionService,
                authService,
                investmentService,
                reportService,
                accountService,
                expenseCategoryDao,
                accountDao
        );

        try {
            MigrationRunner.runMigrations();
            viewHandler.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
