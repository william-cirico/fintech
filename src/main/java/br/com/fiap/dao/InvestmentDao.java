package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Investment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestmentDao implements BaseDao<Investment, Long> {

    @Override
    public List<Investment> findAll() {
        List<Investment> investments = new ArrayList<>();
        String sql = "SELECT * FROM T_FIN_INVESTMENT";
        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                ResultSet resultSet = stm.executeQuery();
                while (resultSet.next()){
                    investments.add(fromResultSet(resultSet));
                }
            }
            return investments;

        }catch (SQLException e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public Investment insert(Investment investment) {
        String sql = "INSERT INTO T_FIN_INVESTMENT (AMOUNT, DATE, TYPE, RISK, LIQUIDITY, DUE_DATE, PROFITABILITY, ACCOUNT_ID)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?,?)";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, investment.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(investment.getDate()));
                stm.setString(3, investment.getInvestmentType());
                stm.setString(4, investment.getRisk());
                stm.setString(5, investment.getLiquidity());
                stm.setDate(6, java.sql.Date.valueOf(investment.getDueDate()));
                stm.setDouble(7, investment.getProfitability());
                stm.setLong(8, investment.getAccountID());
                stm.executeUpdate();
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }
        return findById(investment.getId());
    }

    @Override
    public Investment update(Investment investment) {
        String sql = "UPDATE T_FIN_INVESTMENT SET" +
                " AMOUNT = ?, DATE = ?, TYPE = ?, RISK = ?, LIQUIDITY = ?, DUE_DATE = ?, " +
                "PROFITABILITY = ? WHERE ID = ?";

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

        }catch (SQLException e){
            throw new DatabaseException(e);
        }
        return findById(investment.getId());

    }

    @Override
    public void delete(Investment investment) {
        String sql = "DELETE FROM T_FIN_INVESTMENT WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, investment.getId());
                int rowCount = stm.executeUpdate();
                if(rowCount == 0){
                    throw new EntityNotFoundException(investment.getId());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Investment findById(Long id) {
        String sql = "SELECT * FROM T_FIN_INVESTMENT WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, id);
                ResultSet result = stm.executeQuery();
                if (!result.next()){
                    throw new EntityNotFoundException(id);
                }
                return fromResultSet(result); // returns new investment
            }
        }catch (SQLException e){
            throw new DatabaseException(e);

        }
    }

    private Investment fromResultSet(ResultSet result) throws SQLException{
        return new Investment(
                result.getLong("ID"),
                result.getDouble("AMOUNT"),
                result.getDouble("PROFITABILITY"),
                result.getDate("DATE").toLocalDate(),
                result.getString("TYPE"),
                result.getString("RISK"),
                result.getString("LIQUIDITY"),
                result.getDate("DUE_DATE").toLocalDate(),
                result.getTimestamp("CREATED_AT").toLocalDateTime()
        );

    }

}
