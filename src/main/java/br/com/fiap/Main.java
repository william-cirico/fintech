package br.com.fiap;

import br.com.fiap.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        User user = new User(1, "Teste", "100.100", LocalDate.parse("12/03/2002"), "teste@teste.com", "123456");

        // Criar as categorias
        List<ExpenseCategory> categories = new ArrayList<>(Arrays.asList(
                new ExpenseCategory(1, "Lazer", ExpenseCategoryType.NON_ESSENTIAL),
                new ExpenseCategory(2, "Saúde", ExpenseCategoryType.ESSENTIAL)
        ));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-Vindo a Fintech");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("LOGIN");

        Account account = null;

        // Loop Login
        while(true){
            System.out.println("Digite seu Email: ");
            String email = scanner.nextLine();
            System.out.println("Digite sua Senha: ");
            String senha = scanner.nextLine();

            account = new Account(email, senha);

            if(account.getLogin().autenticar(email, senha)){
                System.out.println("Login realizado com sucesso!");
                break;
            }
            else{
                System.out.println("Email ou senha incorretos, tente novamente");
            }
        }

        // Loop menu de opçōes
        while(true){
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Adicionar Gasto");
            System.out.println("2 - Adicionar Recebimento");
            System.out.println("3 - Adicionar Investimento");
            System.out.println("4 - Exibir Relatório Financeiro");
            System.out.println("5 - Sair");
            System.out.print("Opção: ");

            int opcao = Integer.valueOf(scanner.nextLine());
            if (opcao == 1){
                Transaction gasto = account.validarTransacao(scanner, TransactionType.EXPENSE, categories);
                account.adicionarTransacao(gasto);
                System.out.println("Saldo atual: " + account.getSaldo());
            } else if (opcao == 2) {
                Transaction recebimento = account.validarTransacao(scanner, TransactionType.INCOME, categories);
                account.adicionarTransacao(recebimento);
                System.out.println("Saldo atual: " + account.getSaldo());
            } else if (opcao == 3) {
                Investimento investimento = account.validarInvestimento(scanner);
                account.adicionarInvestimento(investimento);
                System.out.println("Saldo atual: " + account.getSaldo());
            } else if (opcao == 4) {
                account.exibirRelatorio();
            } else if (opcao == 5) {
                System.out.println("Saindo do programa...");
                break;

            }else{
                System.out.println("Opção inválida, tente novamente");
            }
        }
        scanner.close();
    }
}
