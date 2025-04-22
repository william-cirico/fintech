package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.ExpenseCategory;
import br.com.fiap.model.ExpenseCategoryType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseCategoryDao implements BaseDao<ExpenseCategory, Long> {
    @Override
    public List<ExpenseCategory> findAll() {
        List<ExpenseCategory> expenseCategories = new ArrayList<>();
        String sql = "SELECT * FROM T_FIN_EXPENSE_CATEGORY";
        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                ResultSet result = stm.executeQuery();
                while(result.next()){
                    expenseCategories.add(fromResultSet(result));
                }
            }
            return expenseCategories;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public ExpenseCategory insert(ExpenseCategory expenseCategory) {
        String sql = "INSERT INTO T_FIN_EXPENSE_CATEGORY (NAME, TYPE, COLOR, ICON) + VALUES (?, ?, ?, ?)";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setString(1, expenseCategory.getName());
                stm.setString(2, expenseCategory.getType().toString());
                stm.setString(3, expenseCategory.getColor());
                stm.setString(4, expenseCategory.getIcon());
                stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                if(rs.next()) {
                    long generatedId = rs.getLong(1);
                    return findById(generatedId);
                }
            }



        } catch (SQLException e){
            throw new DatabaseException(e);
        }
        return null;
    }


    @Override
    public ExpenseCategory update(ExpenseCategory expenseCategory) {
        String sql = "UPDATE T_FIN_EXPENSE_CATEGORY SET " +
                "NAME = ?, TYPE = ?, COLOR = ?, ICON = ? WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setString(1, expenseCategory.getName());
                stm.setString(2, expenseCategory.getType().toString());
                stm.setString(3, expenseCategory.getColor());
                stm.setString(4, expenseCategory.getIcon());
                stm.setLong(5, expenseCategory.getId());
                stm.executeUpdate();
            }
            return findById(expenseCategory.getId());
        } catch (SQLException e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(ExpenseCategory expenseCategory) {
        String sql = "DELETE FROM T_FIN_EXPENSE_CATEGORY WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, expenseCategory.getId());
                int rowCount = stm.executeUpdate();
                if(rowCount == 0){
                    throw new EntityNotFoundException(expenseCategory.getId());
                }
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }

    }

    @Override
    public ExpenseCategory findById(Long id) {
        String sql = "SELECT * FROM T_FIN_EXPENSE_CATEGORY WHERE ID = ?";

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setLong(1, id);
                ResultSet result = stm.executeQuery();
                if (!result.next()) {
                    throw new EntityNotFoundException(id);
                }
                return fromResultSet(result);

            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private ExpenseCategory fromResultSet(ResultSet result) throws SQLException{
        return new ExpenseCategory(
                result.getLong("ID"),
                result.getString("NAME"),
                ExpenseCategoryType.ESSENTIAL,
                result.getTimestamp("CREATED_AT"),
                result.getString("COLOR"),
                result.getString("ICON")
        );
    }
}
