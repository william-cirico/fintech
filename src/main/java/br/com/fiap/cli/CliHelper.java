package br.com.fiap.cli;

import br.com.fiap.model.*;
import br.com.fiap.service.AuthService;
import br.com.fiap.service.InvestmentService;
import br.com.fiap.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CliHelper {
    private final Scanner scanner = new Scanner(System.in);
    private final List<ExpenseCategory> expenseCategories = new ArrayList<>(Arrays.asList(
            new ExpenseCategory(1, "Lazer", ExpenseCategoryType.NON_ESSENTIAL),
            new ExpenseCategory(2, "Saúde", ExpenseCategoryType.ESSENTIAL)
    ));

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AuthService authService;
    private final TransactionService transactionService;
    private final InvestmentService investmentService;

    private User authenticatedUser = null;

    public CliHelper(TransactionService transactionService, AuthService authService, InvestmentService investmentService) {
        this.transactionService = transactionService;
        this.authService = authService;
        this.investmentService = investmentService;
    }

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
    }

    public ExpenseCategory selectExpenseCategory() {
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

    public LocalDate getParsedDate(String label) {
        while (true) {
            try {
                System.out.println(label);
                return LocalDate.parse(scanner.nextLine(), formatter);
            } catch  (Exception ignored) {
                System.out.println("Data inválida.");
            }
        }
    }

    public void createExpense() {
        Account account = selectAccount();

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

    public void createIncome(){
        Account account = selectAccount();

        System.out.println("Digite o valor do recebimento: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        LocalDate parsedDate = getParsedDate("Digite a data do recebimento (dd/mm/yyyy): ");

        try{
            transactionService.addIncome(account, parsedDate, amount);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showMenu() {
        while (true) {
            System.out.println("\nEscolha uma opção:");

            if(authenticatedUser.getAccounts().size() == 0) {
                System.out.println("1 - Cadastrar conta");

            }
            else {
                System.out.println("1 - Cadastrar conta");
                System.out.println("2 - Adicionar Gasto");
                System.out.println("3 - Adicionar Recebimento");
                System.out.println("4 - Adicionar Investimento");
                System.out.println("5 - Exibir Relatório Financeiro");
                System.out.println("6 - Logout");
                System.out.print("Opção: ");
            }

            int option = Integer.valueOf(scanner.nextLine());

            if(authenticatedUser.getAccounts().isEmpty()){
                switch (option){
                    case 1 -> createAccount();
                    default -> System.out.println("Opção inválida");
                }
            }
            else if(!authenticatedUser.getAccounts().isEmpty()){ // Acrescentado if para ficar com melhor legibilidade.
                switch (option) {
                    case 1 -> createAccount();
                    case 2 -> createExpense();
                    case 3 -> createIncome();
                    case 4 -> createInvestment();
                    case 5 -> showAccountReport();
                    case 6 -> {
                        authenticatedUser = null;
                        System.out.println("Logout realizado!");
                        return;
                    }
                    default -> System.out.println("Opção inválida");
                }
            }
        }

    }

    public void showAccountReport() {
        Account account = selectAccount();
        account.showReport();
    }

    public void loginUser() {
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


    private void createAccount() {
        System.out.println("Digite o nome da conta: ");
        String accountName = scanner.nextLine();
        System.out.println("Digite o saldo inicial: ");
        double balance = Double.valueOf(scanner.nextLine());

        Account newAccount = new Account(UUID.randomUUID().toString(), accountName, balance);
        authenticatedUser.addAccount(newAccount);
    }

    private Account selectAccount() {
        while (true) {
            System.out.println("Selecione a conta:");
            for (int i = 0; i < authenticatedUser.getAccounts().size(); i++) {
                System.out.println((i + 1) + ") " + authenticatedUser.getAccounts().get(i).getName());
            }
            System.out.println("Opção desejada:");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option > authenticatedUser.getAccounts().size() || option < 0) {
                System.out.println("Opção inválida");
                continue;
            }

            return authenticatedUser.getAccounts().get(option - 1);
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


        Account account = selectAccount();


        //Teste construtor básico do investimento e utilização do método adicionarInvestimento.
        System.out.println("Digite o valor do aporte: ");
        double contribution = Double.valueOf(scanner.nextDouble());
        System.out.println("Digite a rentabilidade: ");
        double profitability = Double.valueOf(scanner.nextDouble());
        scanner.nextLine();

        LocalDate date = getParsedDate("Digite a data do investimo (dd/mm/yyyy): ");

        Investment investment = investmentService.addInvestment(account, contribution, profitability, date);

        // Informaçōes adicionais sobre o investimento
        System.out.println("Gostaria de adicionar mais informaçōes sobre o investimento? (Sim ou Não)");
        String resposta = String.valueOf(scanner.nextLine());
        if (resposta.equalsIgnoreCase("Sim")) {
            System.out.println("Tipo do investimento: ");
            String type = scanner.nextLine();
            investment.setInvestmentType(type);
            System.out.println("Risco do investimento");
            String risk = scanner.nextLine();
            investment.setRisk(risk);
            System.out.println("Liquidez");
            String liquid = scanner.nextLine();
            investment.setLiquidity(liquid);

            LocalDate dueDate = getParsedDate("Digite a data de vencimento (dd/mm/yyyy): ");
            investment.setDueDate(dueDate);

        } else if (resposta.equalsIgnoreCase("Não") || resposta.equalsIgnoreCase("Nao")) {
            System.out.println("Dados não preenchidos permanecerão vazios.");
        } else {
            System.out.println("Resposta inválida. Considerando como 'Não'.");
        }
    }
}
