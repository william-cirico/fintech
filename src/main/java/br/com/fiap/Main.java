package br.com.fiap;

import br.com.fiap.cli.CLIHandler;
import br.com.fiap.service.AuthService;
import br.com.fiap.service.InvestmentService;
import br.com.fiap.service.ReportService;
import br.com.fiap.service.TransactionService;
import br.com.fiap.utils.MigrationRunner;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        TransactionService transactionService = new TransactionService();
        InvestmentService investmentService = new InvestmentService();
        ReportService reportService = new ReportService();

        CLIHandler cliHandler = new CLIHandler(transactionService, authService, investmentService, reportService);

        try {
            MigrationRunner.runMigrations();
            cliHandler.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
