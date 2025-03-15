package br.com.fiap.service;

import br.com.fiap.model.Investment;
import br.com.fiap.model.Account;

import java.time.LocalDate;

public class InvestmentService {
    public Investment addInvestment(Account account, double contribution, double profitability, LocalDate date) {
        if (!account.withdraw(contribution)) throw new IllegalArgumentException("Saldo insuficiente para realizar o investimento desejado.");

        Investment investment = new Investment(contribution, profitability,date);

        account.addInvestment(investment);

        return investment;
    }
}
