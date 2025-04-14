package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Investment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class InvestmentDao implements BaseDao<Investment, Long> {

    @Override
    public void insert(Investment investment) {
        String sql = "INSERT INTO T_FIN_INVESTMENT (amount, date, type, risk, liquidity, due_date, profitability)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, investment.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(investment.getDate()));
                stm.setString(3, investment.getInvestmentType());
                stm.setString(4, investment.getRisk());
                stm.setString(5, investment.getLiquidity());
                stm.setDate(6, java.sql.Date.valueOf(investment.getDueDate()));
                stm.setDouble(7, investment.getProfitability());
                stm.executeUpdate();
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(Investment entity) {

    }

    @Override
    public void delete(Investment entity) {

    }

    @Override
    public Investment findById(Long aLong) {
        return null;
    }

    @Override
    public List<Investment> findAll() {
        return List.of();
    }
}
