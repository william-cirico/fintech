package br.com.fiap.service;

import br.com.fiap.dao.InvestmentDao;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Investment;
import br.com.fiap.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class InvestmentService {
    private final InvestmentDao investmentDao;

    public InvestmentService(InvestmentDao investmentDao) {
        this.investmentDao = investmentDao;
    }

    public Investment addInvestment(Account account, double amount, LocalDate date, String investmentType, String risk, String liquidity, LocalDate dueDate, double profitability, long account_id) {
        if (!account.withdraw(amount) || profitability < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o investimento desejado.");
        }

        Investment investment = new Investment(amount, date, investmentType, risk, liquidity, dueDate, profitability, account_id);
        investmentDao.insert(investment);
        account.addInvestment(investment);

        return investment;
    }
}
