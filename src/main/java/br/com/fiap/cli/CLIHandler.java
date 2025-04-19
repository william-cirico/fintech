package br.com.fiap.cli;

import br.com.fiap.dao.AccountDao;
import br.com.fiap.dao.UserDao;
import br.com.fiap.exceptions.OptionalAccountInvalidException;
import br.com.fiap.model.*;
import br.com.fiap.service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CLIHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final List<ExpenseCategory> expenseCategories = new ArrayList<>(Arrays.asList(
            new ExpenseCategory(null, "Lazer", ExpenseCategoryType.NON_ESSENTIAL),
            new ExpenseCategory(null, "Saúde", ExpenseCategoryType.ESSENTIAL)
    ));
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AuthService authService;
    private final TransactionService transactionService;
    private final InvestmentService investmentService;
    private final ReportService reportService;
    private final AccountService accountService;

    private User authenticatedUser = null;

    public void run() {
        while (true) {
            System.out.println("Bem-Vindo ");
            System.out.println("1) Criar usuário");
            System.out.println("2) Fazer login");
            System.out.println("3) Sair");
            System.out.println("Digite a opção desejada: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createUser();
                case 2 -> loginUser();
                case 3 -> System.exit(0);
                default -> System.out.println("Opção inválida");
            }
        }
    }public CLIHandler(TransactionService transactionService, AuthService authService, InvestmentService investmentService, ReportService reportService, AccountService accountService) {
             this.transactionService = transactionService;
             this.authService = authService;
             this.investmentService = investmentService;
             this.reportService = reportService;
             this.accountService = accountService;
         }

    private ExpenseCategory selectExpenseCategory() {
        while (true) {
            System.out.println("Selecione a categoria do gasto:");
            for (int i = 0; i < expenseCategories.size(); i++) {
                System.out.println((i + 1) + ") " + expenseCategories.get(i).getName());
            }
            System.out.println("Opção desejada:");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option > expenseCategories.size() || option < 0) {
                continue;
            }

            return expenseCategories.get(option - 1);
        }
    }

    private LocalDate getParsedDate(String label) {
        while (true) {
            try {
                System.out.println(label);
                return LocalDate.parse(scanner.nextLine(), formatter);
            } catch (Exception ignored) {
                System.out.println("Data inválida.");
            }
        }
    }

    private void createExpense() {
        Account account = selectAccount("Seleciona a conta: ");

        System.out.println("Digite o valor do gasto: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        LocalDate date = getParsedDate("Digite a data do gasto (dd/mm/yyyy): ");
        ExpenseCategory expenseCategory = selectExpenseCategory();

        try {
            transactionService.addExpense(account, date, amount, expenseCategory);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createIncome() {
        Account account = selectAccount("Selecione a conta: ");

        System.out.println("Digite o valor do recebimento: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        LocalDate parsedDate = getParsedDate("Digite a data do recebimento (dd/mm/yyyy): ");

        try {
            transactionService.addIncome(account, parsedDate, amount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showMenu() {
        while (true) {
            System.out.println("\nEscolha uma opção:");

            if (authenticatedUser.getAccounts().isEmpty()) {
                System.out.println("1 - Cadastrar conta");

            } else {
                System.out.println("1 - Cadastrar conta");
                System.out.println("2 - Adicionar Gasto");
                System.out.println("3 - Adicionar Recebimento");
                System.out.println("4 - Adicionar Transferência");
                System.out.println("5 - Adicionar Investimento");
                System.out.println("6 - Relatórios");
                System.out.println("7 - Alterar dados");
                System.out.println("8 - Logout");
                System.out.print("Opção: ");
            }

            int option = Integer.parseInt(scanner.nextLine());

            if (authenticatedUser.getAccounts().isEmpty()) {
                if (option == 1) {
                    createAccount();
                } else {
                    System.out.println("Opção inválida");
                }
            } else {
                switch (option) {
                    case 1 -> createAccount();
                    case 2 -> createExpense();
                    case 3 -> createIncome();
                    case 4 -> createTransfer();
                    case 5 -> createInvestment();
                    case 6 -> showReportsMenu();
                    case 7 -> updateUser();
                    case 8 -> {
                        authenticatedUser = null;
                        System.out.println("Logout realizado!");
                        return;
                    }
                    default -> System.out.println("Opção inválida");
                }
            }
        }

    }

    private void showReportsMenu() {
        while (true) {
            System.out.println("=== Relatórios ===");
            System.out.println("1) Relatório Geral");
            System.out.println("2) Relatório de Despesas");
            System.out.println("3) Relatório de Receitas");
            System.out.println("4) Relatório de Transferências");
            System.out.println("5) Relatório de Despesas Essenciais");
            System.out.println("6) Relatório de Despesas Não Essenciais");
            System.out.println("7) Rank de Despesas por Categoria");
            System.out.println("8) Sair");
            System.out.println("Digite a opção: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 8) {
                break;
            }

            // Obtendo o intervalo de datas do último mês
            LocalDate startDate = LocalDate.now().minusMonths(1);
            LocalDate endDate = LocalDate.now();

            Account account = selectAccount("Escolha uma conta para gerar o relatório: ");

            switch (option) {
                case 1 -> reportService.printAccountReport(account);
                case 2 -> reportService.printExpenseReportByPeriodFromAccount(account, startDate, endDate);
                case 3 -> reportService.printIncomeReportByPeriodFromAccount(account, startDate, endDate);
                case 4 -> reportService.printTransferReportByPeriodFromAccount(account, startDate, endDate);
                case 5 -> reportService.printTotalExpensesByCategoryTypeAndPeriodFromAccount(account, ExpenseCategoryType.ESSENTIAL, startDate, endDate);
                case 6 -> reportService.printTotalExpensesByCategoryTypeAndPeriodFromAccount(account, ExpenseCategoryType.NON_ESSENTIAL, startDate, endDate);
                case 7 -> reportService.printTopExpenseCategoriesByPeriod(account, startDate, endDate);
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void loginUser() {
        System.out.println("--- Login ---");
        System.out.println("Digite o seu e-mail: ");
        String username = scanner.nextLine();

        System.out.println("Digite a sua senha: ");
        String password = scanner.nextLine();

        try {
            authenticatedUser = authService.login(username, password);
            System.out.println("Login bem-sucedido!");
            showMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTransfer() {
        if (authenticatedUser.getAccounts().size() < 2) {
            System.out.println("Para realizar uma transferência é necessário possuir pelo menos duas contas");
            return;
        }

        Account from = selectAccount("Selecione a conta de origem: ");
        Account to = selectAccount("Selecione a conta de destino: ");

        System.out.println("Digite o valor que será transferido: ");
        double amount = Double.parseDouble(scanner.nextLine());

        LocalDate date = getParsedDate("Digite a data da transferência (dd/mm/yyyy): ");

        try {
            transactionService.addTransfer(from, to, date, amount);
            System.out.println("Transferência realizada com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createAccount() {
        System.out.println("Digite o nome da conta: ");
        String accountName = scanner.nextLine();


        double balance;
        do {
            System.out.println("Digite o saldo inicial: ");
            balance = Double.parseDouble(scanner.nextLine());

            if (balance < 0) {
                System.out.println("O saldo não pode ser negativo, tente novamente");
            }
        } while (balance < 0);

        Account newAccount = new Account(null, accountName, balance, null, null);
        accountService.addAccount(newAccount);
    }

    private Account selectAccount(String label) {
        while (true) {
            System.out.println(label);
            List<Account> accounts = accountService.findUserAccounts(authenticatedUser);

            for(Account account: accounts){
                System.out.println("Id: " + account.getId());
                System.out.println("Name: "+ account.getName());
            }

            System.out.println("Opção desejada:");
            int option = scanner.nextInt();
            scanner.nextLine();

            // Verificando se o ID selecionado existe nas contas
            Optional<Account> selectedAccount = accounts.stream().filter(a -> a.getId() == option).findFirst();

            if (selectedAccount.isEmpty()) {
                throw new OptionalAccountInvalidException(option);
            }
            return selectedAccount.get();
        }
    }

    private void createUser() {
        System.out.println("Digite seu Nome: ");
        String name = scanner.nextLine();
        System.out.println("Digite seu Email: ");
        String username = scanner.nextLine();
        System.out.println("Digite seu CPF");
        String cpf = scanner.nextLine();
        System.out.println("Digite sua Senha: ");
        String password = scanner.nextLine();
        System.out.println("Confirme sua Senha: ");
        String passwordConfirmation = scanner.nextLine();

        try {
            authService.registerUser(name, cpf, username, password, passwordConfirmation);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createInvestment() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Account View");
        Scanner scanner = new Scanner(System.in);


        Account account = selectAccount("Selecione a conta: ");


        //Teste construtor básico do investimento e utilização do método adicionarInvestimento.
        System.out.println("Digite o valor do aporte: ");
        double contribution = Double.parseDouble(scanner.nextLine());
        System.out.println("Digite a rentabilidade: ");
        double profitability = Double.parseDouble(scanner.nextLine());

        LocalDate date = getParsedDate("Digite a data do investimo (dd/mm/yyyy): ");



        // Informaçōes adicionais sobre o investimento
        System.out.println("Gostaria de adicionar mais informaçōes sobre o investimento? (Sim ou Não)");
        String resposta = String.valueOf(scanner.nextLine());
        if (resposta.equalsIgnoreCase("Sim")) {
            System.out.println("Tipo do investimento: ");
            String type = scanner.nextLine();
            System.out.println("Risco do investimento");
            String risk = scanner.nextLine();
            System.out.println("Liquidez");
            String liquidity = scanner.nextLine();

            LocalDate dueDate = getParsedDate("Digite a data de vencimento (dd/mm/yyyy): ");

            Investment investment = investmentService.addInvestment(account, contribution, date, type, risk, liquidity, dueDate,  profitability, account.getId());

        } else if (resposta.equalsIgnoreCase("Não") || resposta.equalsIgnoreCase("Nao")) {
            System.out.println("Dados não preenchidos permanecerão vazios.");
        } else {
            System.out.println("Resposta inválida. Considerando como 'Não'.");
        }
    }

    private void updateUser() {
        String newName;
        String newCpf;
        String newPassword;
        String confirmationNewPassword;
        String newUsername;
        System.out.println("--- Alteração dos dados ---");
        System.out.println("Digite a sua senha atual:");
        String password = scanner.nextLine();

        try {
            authenticatedUser = authService.login(authenticatedUser.getUsername(), password);
            System.out.println("Senha correta, digite os novos valores:");

            System.out.println("Deseja alterar o nome na conta? (Sim/Não)");
            String alterName = String.valueOf(scanner.nextLine());
            if(alterName.equalsIgnoreCase("Sim")){
                System.out.println("Digite o novo nome:");
                newName = scanner.nextLine();
                authenticatedUser.setName(newName);
            }
            System.out.println("Deseja alterar o cpf? (Sim/Não)");
            String alterCpf = String.valueOf(scanner.nextLine());
            if(alterCpf.equalsIgnoreCase("Sim")){
                System.out.println("Digite o novo cpf:");
                newCpf = scanner.nextLine();
                authenticatedUser.setCpf(newCpf);
            }
            System.out.println("Deseja alterar a senha? (Sim/Não)");
            boolean decisionAlterPassword = false;
            String alterPassword = String.valueOf(scanner.nextLine());
            if(alterPassword.equalsIgnoreCase("Sim")){
                decisionAlterPassword = true;
                System.out.println("Digite a nova senha:");
                newPassword = scanner.nextLine();
                System.out.println("Confirme a nova senha:");
                confirmationNewPassword = scanner.nextLine();

                if(!newPassword.equals(confirmationNewPassword)){
                    System.out.println("As senhas não coincidem, o processo será reiniciado.");
                    updateUser();
                }
                authenticatedUser.setPassword(newPassword);
            }
            System.out.println("Deseja alterar o username? (Sim/Não)");
            String alterUsername = String.valueOf(scanner.nextLine());
            if(alterUsername.equalsIgnoreCase("Sim")){
                System.out.println("Digite o novo username:");
                newUsername = scanner.nextLine();
                authenticatedUser.setUsername(newUsername);
            }
            authService.
                    updateUser(authenticatedUser, decisionAlterPassword);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
