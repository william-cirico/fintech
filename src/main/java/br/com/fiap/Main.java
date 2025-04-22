package br.com.fiap;

import br.com.fiap.cli.CLIHandler;
import br.com.fiap.service.*;
import br.com.fiap.utils.MigrationRunner;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        TransactionService transactionService = new TransactionService();
        InvestmentService investmentService = new InvestmentService();
        ReportService reportService = new ReportService();
        AccountService accountService = new AccountService();

        CLIHandler cliHandler = new CLIHandler(transactionService, authService, investmentService, reportService, accountService);

        try {
            MigrationRunner.runMigrations();
            cliHandler.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
