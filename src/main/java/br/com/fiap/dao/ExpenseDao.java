package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Expense;
import br.com.fiap.model.ExpenseCategory;
import br.com.fiap.model.ExpenseCategoryType;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDao implements BaseDao<Expense, Long>{

    @Override
    public List<Expense> findAll() {
        List<Expense> expenses = new ArrayList<>();
        String sql = """
            SELECT 
                e.*,
                ec.id expense_category_id,
                ec.name expense_category_name,
                ec.type expense_category_type,
                ec.created_at expense_category_created_at,
                ec.color expense_category_color,
                e.icon expense_category_icon
            FROM T_FIN_EXPENSE e 
            INNER JOIN T_FIN_EXPENSE_CATEGORY ec ON ec.id = e.category_id
        """;

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                ResultSet result = stm.executeQuery();
                while(result.next()){
                    expenses.add(fromResultSet(result));
                }
            }
            return expenses;
        }catch (SQLException e){
            throw new DatabaseException(e);
        }

    }

    @Override
    public Expense insert(Expense expense) {
        String sql = "INSERT INTO T_FIN_EXPENSE (AMOUNT, DATE, DESCRIPTION, OBSERVATION, ACCOUNT_ID, CATEGORY_ID) +" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, expense.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(expense.getDate()));
                stm.setString(3, expense.getDescription());
                stm.setString(4, expense.getObservations());
                stm.setLong(5, expense.getOriginAccountId());
                stm.setLong(6, expense.getCategory().getId());
            }
            return findById(expense.getId());

        }catch (SQLException e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public Expense update(Expense expense) {
        String sql = "UPDATE T_FIN_EXPENSE SET " +
                "AMOUNT = ?, DATE = ?, DESCRIPTION = ?, OBSERVATIONS = ?, ACCOUNT_ID = ?, CATEGORY_ID = ?" +
                "WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, expense.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(expense.getDate()));
                stm.setString(3, expense.getDescription());
                stm.setString(4, expense.getObservations());
                stm.setLong(5, expense.getOriginAccountId());
                stm.setLong(6, expense.getCategory().getId());

                stm.executeUpdate();
            }

            return findById(expense.getId());
        }catch (SQLException e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Expense expense) {
        String sql = "DELETE T_FIN_EXPENSE WHERE ID = ?";

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setLong(1, expense.getId());
                int rowCount = stm.executeUpdate();
                if (rowCount == 0) {
                    throw new EntityNotFoundException(expense.getId());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);

        }
    }

    @Override
    public Expense findById(Long id) {
        return null;
    }

    private Expense fromResultSet(ResultSet result) throws SQLException{
        return new Expense(
                result.getLong("ID"),
                result.getDouble("AMOUNT"),
                result.getDate("DATE").toLocalDate(),
                result.getString("DESCRIPTION"),
                result.getString("OBSERVATIONS"),
                result.getTimestamp("CREATED_AT").toLocalDateTime(),
                new ExpenseCategory(
                        result.getLong("EXPENSE_CATEGORY_ID"),
                        result.getString("EXPENSE_CATEGORY_NAME"),
                        ExpenseCategoryType.valueOf(result.getString("EXPENSE_CATEGORY_TYPE")),
                        result.getTimestamp("EXPENSE_CATEGORY_CREATED_AT"),
                        result.getString("EXPENSE_CATEGORY_COLOR"),
                        result.getString("EXPENSE_CATEGORY_ICON")
                ),
                result.getLong("ORIGIN_ACCOUNT_ID")
        );
    }


}
