package br.com.fiap.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Account {
    private String name;
    private double balance;
    private List<Transaction> transactions = new ArrayList<>();
    private List<Investimento> investments = new ArrayList<>();

    public Account(String name, double balance) {
        this.balance = balance;
        this.name = name;
    }

    public void addTransaction(Transaction transaction) {
        balance += transaction.getAmount();
        transactions.add(transaction);
    }

    public void adicionarInvestimento(Investimento investimento) {
        if (investimento.getValorAporte() > balance) {
            System.out.println("Saldo insuficiente");
            return;
        }
        balance -= investimento.getValorAporte();
        investments.add(investimento);
        System.out.println("Investimento realizado com sucesso!");
    }

//    public Transaction validarTransacao(Scanner scanner, TransactionType type, List<ExpenseCategory> categories) {
//        System.out.println("Valor: ");
//        Double valor = Double.valueOf(scanner.nextLine());
//        System.out.println("Data: (dd/mm/yyyy) ");
//        String data = scanner.nextLine();
//
//        // Formata a data
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        LocalDate date = null;
//        try {
//            date = LocalDate.parse(data, formatter);
//        } catch (DateTimeParseException e) {
//            System.out.println("Formato invalido. Use dd/mm/yyyy.");
//        }
//        System.out.println("Descrição: ");
//        String descricao = scanner.nextLine();
//
//        // Valida se a transação é um gasto ou recebimento
//        if (type == TransactionType.EXPENSE) {
//            System.out.println("Escolha a categoria do gasto: ");
//            for (ExpenseCategory category : categories) {
//                System.out.println(category.getId() + ") " + category.getName());
//            }
//
//            int categoryId = Integer.valueOf(scanner.nextLine());
//
//            // Buscar na lista de categorias a categoria com o id informado
//            Optional<ExpenseCategory> category =
//                    categories.stream().filter(c -> c.getId() == categoryId).findFirst();
//
//            if (category.isEmpty()) {
//                throw new IllegalArgumentException("Categoria escolhida é inválida");
//            }
//            return new Expense(valor, date, category.get());
//        } else {
//            return new Income(valor, date, descricao);
//        }
//    }


    public Investimento validarInvestimento(Scanner scanner) {
        System.out.println("Valor:");
        double valor = Double.valueOf(scanner.nextLine());
        System.out.println("Rentabilidade: ");
        double rentabilidade = Double.valueOf(scanner.nextLine());

        // Formata a data
        System.out.println("Data: (dd/mm/yyyy) ");
        String data = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = null;
        try {
            date = LocalDate.parse(data, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Formato invalido. Use dd/mm/yyyy.");
        }

        // Informaçōes adicionais sobre o investimento
        System.out.println("Gostaria de adicionar mais informaçōes sobre o investimento? (Sim ou Não)");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("Sim")) {
            System.out.println("Tipo do investimento: ");
            String tipo = scanner.nextLine();
            System.out.println("Risco do investimento");
            String risco = scanner.nextLine();
            System.out.println("Liquidez");
            String liquidez = scanner.nextLine();

            // Formata a data do vencimento
            System.out.println("Data de vencimento (dd/mm/yyyy)");
            String dataVencimento = scanner.nextLine();
            LocalDate dateVencimento = null;
            try {
                dateVencimento = LocalDate.parse(dataVencimento, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use dd/mm/yyyy.");
            }

            // Se mais informaçōes forem dadas, retorna construtor com todas as informacōes
            return new Investimento(valor, rentabilidade, date, tipo, risco, liquidez, dateVencimento);

            // Se não retorna construtor simplificado
        } else if (resposta.equalsIgnoreCase("Não") || resposta.equalsIgnoreCase("Nao")) {
            return new Investimento(valor, rentabilidade, date);
        } else {
            System.out.println("Resposta inválida. Considerando como 'Não'.");
            return new Investimento(valor, rentabilidade, date);
        }
    }

    public void exibirRelatorio(Account account) {
        ArrayList<Transaction> gastos = new ArrayList<>();
        ArrayList<Transaction> recebimentos = new ArrayList<>();

        for (Transaction transaction : this.transactions) {
            List<Transaction> expenses = transactions.stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE) // Filtra todas as transações que são instancias da classe Expense
                    .collect(Collectors.toList());

            List<Transaction> incomes = transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).collect(Collectors.toList());

            for(Transaction expense : expenses)  {
                gastos.add(expense);
            }

            for(Transaction income : incomes)  {
                recebimentos.add(income);

            }





        }

//        public Boolean filtrarDespesa(Transaction t) {
//                    return t instanceof Expense;
//                }


        List<Transaction> incomes = transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).collect(Collectors.toList());

        // Exibe os gastos
        System.out.println("\nTransações:");
        System.out.println("===============================================================");
        System.out.println("\nGastos: ");
        for (Transaction gasto : gastos) {
            System.out.println(gasto);
        }
        System.out.println("===============================================================");
        // Exibe os recebimentos
        System.out.println("\nRecebimentos:");
        for (Transaction recebimento : recebimentos) {
            System.out.println(recebimento);
        }
        System.out.println("===============================================================");
        // Exibe investimentos
        System.out.println("\nInvestimentos");
        for (Investimento investimento : investments) {
            System.out.println(investimento);
        }
        // Saldo
        System.out.println("Saldo da conta: " + this.balance);

    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
