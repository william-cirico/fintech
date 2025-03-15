package br.com.fiap;

import br.com.fiap.cli.CliHelper;
import br.com.fiap.model.*;
import br.com.fiap.service.AuthService;
import br.com.fiap.service.InvestmentService;
import br.com.fiap.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        TransactionService transactionService = new TransactionService();
        InvestmentService investmentService = new InvestmentService();

        CliHelper cliHelper = new CliHelper(transactionService, authService, investmentService);
        cliHelper.run();
    }
}
