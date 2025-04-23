package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Account;
import br.com.fiap.model.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

    public List<Income> findAllByAccountId(Long accountId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM T_FIN_INCOME WHERE ACCOUNT_ID = ?";
        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, accountId);
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

    public double getTotalByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        String sql = """
            SELECT COALESCE(SUM(amount), 0)
            FROM T_FIN_INCOME            
            WHERE account_id = ?            
            AND "DATE" BETWEEN ? AND ?
        """;

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, account.getId());
                stm.setDate(2, java.sql.Date.valueOf(start));
                stm.setDate(3, java.sql.Date.valueOf(end));

                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return 0;
    }

    public List<Income> findAllByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Income> incomes = new ArrayList<>();

        String sql = """
            SELECT * FROM T_FIN_INCOME               
            WHERE account_id = ?            
            AND "DATE" BETWEEN ? AND ?
        """;

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, account.getId());
                stm.setDate(2, java.sql.Date.valueOf(start));
                stm.setDate(3, java.sql.Date.valueOf(end));

                ResultSet rs = stm.executeQuery();
                while(rs.next()){
                    incomes.add(fromResultSet(rs));
                }

                return incomes;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Income insert(Income income) {
        String sql = """
            INSERT INTO T_FIN_INCOME (AMOUNT, "DATE", DESCRIPTION, OBSERVATION, ACCOUNT_ID) VALUES (?, ?, ?, ?, ?)
        """;

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, income.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(income.getDate()));
                stm.setString(3, income.getDescription());
                stm.setString(4, income.getObservation());
                stm.setLong(5, income.getOriginAccountId());

                stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    return findById(generatedId);
                }
            }
        }catch (SQLException e){
            throw new DatabaseException(e);
        }

        return null;
    }

    @Override
    public Income update(Income income) {
        String sql = "UPDATE T_FIN_INCOME SET" +
                "AMOUNT = ?, DATE = ?, DESCRIPTION = ?, OBSERVATIONS= ? WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, income.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(income.getDate()));
                stm.setString(3, income.getDescription());
                stm.setString(4, income.getObservation());
                stm.setLong(5, income.getId());

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
                result.getString("DESCRIPTION"),
                result.getString("OBSERVATION"),
                result.getTimestamp("CREATED_AT").toLocalDateTime(),
                result.getLong("ACCOUNT_ID")
        );
    }
}
