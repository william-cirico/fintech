package br.com.fiap;

import br.com.fiap.model.*;
import br.com.fiap.service.AuthService;
import br.com.fiap.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    private static final List<ExpenseCategory> expenseCategories = new ArrayList<>(Arrays.asList(
            new ExpenseCategory(1, "Lazer", ExpenseCategoryType.NON_ESSENTIAL),
            new ExpenseCategory(2, "Saúde", ExpenseCategoryType.ESSENTIAL)

    ));

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final static AuthService authService = new AuthService();

    private static User authenticatedUser = null;

    public static void main(String[] args) {
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

    public static ExpenseCategory selectExpenseCategory() {
        while (true) {
            System.out.println("Selecione a categoria do gasto:");
            for (int i = 0; i < expenseCategories.size(); i++) {
                System.out.println((i + 1) + ") " + expenseCategories.get(i).getName());
            }
            System.out.println("Opção desejada:");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option >= expenseCategories.size() || option < 0) {
                continue;
            }

            return expenseCategories.get(option);
        }
    }

    public static void showMenu() {
        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Adicionar Gasto");
            System.out.println("2 - Adicionar Recebimento");
            System.out.println("3 - Adicionar Investimento");
            System.out.println("4 - Exibir Relatório Financeiro");
            System.out.println("5 - Logout");
            System.out.print("Opção: ");

            int option = Integer.valueOf(scanner.nextLine());

            switch (option) {

                case 1 ->{}
                case 2 ->{}
                case 3 ->{viewAccount();}
                case 4 ->{}
                case 5 -> {
                    authenticatedUser = null;
                    System.out.println("Logout realizado!");
                    return;
                }
            }
        }

    }

    public static void main(String[] args) {

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

    public static void loginUser() {
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
//
//        // Loop menu de opçōes
//        while(true){
//
//
//            int opcao = Integer.valueOf(scanner.nextLine());
//            if (opcao == 1){
//                Transaction gasto = account.validarTransacao(scanner, TransactionType.EXPENSE, categories);
//                account.adicionarTransacao(gasto);
//                System.out.println("Saldo atual: " + account.getSaldo());
//            } else if (opcao == 2) {
//                Transaction recebimento = account.validarTransacao(scanner, TransactionType.INCOME, categories);
//                account.adicionarTransacao(recebimento);
//                System.out.println("Saldo atual: " + account.getSaldo());
//            } else if (opcao == 3) {
//                Investimento investimento = account.validarInvestimento(scanner);
//                account.adicionarInvestimento(investimento);
//                System.out.println("Saldo atual: " + account.getSaldo());
//            } else if (opcao == 4) {
//                account.exibirRelatorio();
//            } else if (opcao == 5) {
//                System.out.println("Saindo do programa...");
//                break;
//
//            }else{
//                System.out.println("Opção inválida, tente novamente");
//            }
//        }
//        scanner.close();
//    }

    private static void createUser() {
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


    private static void viewAccount() {

        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Account View");
        Scanner scanner = new Scanner(System.in);

        // Teste construtor básico account
        System.out.println("Digite o nome: ");
        String name = scanner.nextLine();
        System.out.println("Digite o valor: ");
        double balance = scanner.nextDouble();

        Account account = new Account(UUID.randomUUID().toString(), name, balance);


        //Teste construtor básico do investimento e utilização do método adicionarInvestimento.
        System.out.println("Digite o valor do aporte: ");
        double contribution = Double.valueOf(scanner.nextDouble());
        System.out.println("Digite a rentabilidade: ");
        double profitability = Double.valueOf(scanner.nextDouble());
        scanner.nextLine();


        LocalDate date = null;
        boolean dataValida = false;

        while (!dataValida) {
            System.out.println("Digite a data (yyyy-mm-dd):");
            String input = scanner.nextLine();

            try {
                 date = LocalDate.parse(input,formatter);
                dataValida = true;
            } catch (Exception e) {
                System.out.println("Data inválida. Tente novamente.");
            }
        }


        Investimento investimento = new Investimento(contribution, profitability,date);

        // Informaçōes adicionais sobre o investimento
        System.out.println("Gostaria de adicionar mais informaçōes sobre o investimento? (Sim ou Não)");
        String resposta = String.valueOf(scanner.nextLine());
        if (resposta.equalsIgnoreCase("Sim")) {
            System.out.println("Tipo do investimento: ");
            String type = scanner.nextLine();
            investimento.setTipoInvestimento(type);
            System.out.println("Risco do investimento");
            String risk = scanner.nextLine();
            investimento.setRisco(risk);
            System.out.println("Liquidez");
            String liquid = scanner.nextLine();
            investimento.setLiquidez(liquid);

            // Formata a data do vencimento

            dataValida = false;

            while(!dataValida){
                try{
                    System.out.println("Data de vencimento (dd/mm/yyyy)");
                    LocalDate dueDate = LocalDate.parse(scanner.nextLine(), formatter);
                    dataValida = true;

                } catch (Exception e){
                    System.out.println("Data inválida. Tente novamente.");
                }
            }
        } else if (resposta.equalsIgnoreCase("Não") || resposta.equalsIgnoreCase("Nao")) {
            System.out.println("Dados não preenchidos permanecerão vazios.");
        } else {
            System.out.println("Resposta inválida. Considerando como 'Não'.");
        }
        System.out.println(investimento.toString());
        account.exibirRelatorio(account);

    }
}
