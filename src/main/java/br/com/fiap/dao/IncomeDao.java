package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IncomeDao implements BaseDao<Income, Long>{

    @Override
    public List<Income> findAll() {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM T_FIN_INCOME";
        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                ResultSet result = stm.executeQuery();
                while(result.next()){
                    incomes.add(fromResultSet(result));
                }
            }
            return incomes;

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Income insert(Income income) {
        String sql = "INSERT INTO T_FIN_INCOME (AMOUNT, DATE, DESCRIPTION) VALUES (?, ?, ?)";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, income.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(income.getDate()));
                stm.setString(3, income.getDescription());
            }
        }catch (SQLException e){
            throw new DatabaseException(e);
        }
        return findById(income.getId());
    }

    @Override
    public Income update(Income income) {
        String sql = "UPDATE T_FIN_INCOME SET" +
                "AMOUNT = ?, DATE = ?, DESCRIPTION = ? WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, income.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(income.getDate()));
                stm.setString(3, income.getDescription());
                stm.setLong(4, income.getId());

                stm.executeUpdate();
            }
        }catch (SQLException e){
            throw new DatabaseException(e);
        }

        return findById(income.getId());
    }

    @Override
    public void delete(Income income) {
        String sql = "DELETE FROM T_FIN_INCOME WHERE ID = ?";

        try (Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, income.getId());
                int rowCount = stm.executeUpdate();
                if(rowCount == 0){
                    throw new EntityNotFoundException(income.getId());
                }
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }

    }

    @Override
    public Income findById(Long id) {
        String sql = "SELECT * FROM T_FIN_INCOME WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, id);
                ResultSet result = stm.executeQuery();
                if(!result.next()){
                    throw new EntityNotFoundException(id);
                }
                return fromResultSet(result);
            }
        }catch (SQLException e){
            throw new DatabaseException(e);
        }
    }

    private Income fromResultSet(ResultSet result) throws SQLException{
        return new Income(
                result.getLong("ID"),
                result.getDouble("AMOUNT"),
                result.getDate("DATE").toLocalDate(),
                result.getString("DESCRIPTION")
        );
    }


}
