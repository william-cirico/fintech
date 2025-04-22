package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDao implements BaseDao<Expense, Long> {

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
                        ec.icon expense_category_icon
                    FROM T_FIN_EXPENSE e 
                    INNER JOIN T_FIN_EXPENSE_CATEGORY ec ON ec.id = e.category_id
                """;

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                ResultSet result = stm.executeQuery();
                while (result.next()) {
                    expenses.add(fromResultSet(result));
                }
            }
            return expenses;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

    }

    public List<Expense> findAllByAccountId(Long accountId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = """
                    SELECT 
                        e.*,
                        ec.id EXPENSE_CATEGORY_ID,
                        ec.name EXPENSE_CATEGORY_NAME,
                        ec.type EXPENSE_CATEGORY_TYPE,
                        ec.created_at EXPENSE_CATEGORY_CREATED_AT,
                        ec.color EXPENSE_CATEGORY_COLOR,
                        ec.icon EXPENSE_CATEGORY_ICON
                    FROM T_FIN_EXPENSE e 
                    INNER JOIN T_FIN_EXPENSE_CATEGORY ec ON ec.id = e.category_id
                    WHERE e.account_id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setLong(1, accountId);
                ResultSet result = stm.executeQuery();
                while (result.next()) {
                    expenses.add(fromResultSet(result));
                }
            }
            return expenses;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

    }

    @Override
    public Expense insert(Expense expense) {
        String sql = """
                    INSERT INTO T_FIN_EXPENSE (AMOUNT, "DATE", DESCRIPTION, OBSERVATION, ACCOUNT_ID, CATEGORY_ID) 
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        System.out.println(expense);

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setDouble(1, expense.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(expense.getDate()));
                stm.setString(3, expense.getDescription());
                stm.setString(4, expense.getObservation());
                stm.setLong(5, expense.getOriginAccountId());
                stm.setLong(6, expense.getCategory().getId());
                stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    return findById(generatedId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return null;
    }

    public double getTotalByCategoryTypeAndPeriodFromAccount(Account account, ExpenseCategoryType type, LocalDate start, LocalDate end) {
        String sql = """
                    SELECT COALESCE(SUM(e.amount), 0)
                    FROM T_FIN_EXPENSE e
                    INNER JOIN T_FIN_EXPENSE_CATEGORY c ON e.category_id = c.id
                    WHERE e.account_id = ?
                    AND c.type = ?
                    AND e."DATE" BETWEEN ? AND ?
                """;

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setLong(1, account.getId());
                stm.setString(2, type.toString());
                stm.setDate(3, java.sql.Date.valueOf(start));
                stm.setDate(4, java.sql.Date.valueOf(end));

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

    public double getTotalByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        String sql = """
                    SELECT COALESCE(SUM(amount), 0)
                    FROM T_FIN_EXPENSE            
                    WHERE account_id = ?            
                    AND "DATE" BETWEEN ? AND ?
                """;

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
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

    public List<Expense> findAllByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Expense> expenses = new ArrayList<>();

        String sql = """
                    SELECT 
                        e.*,
                        ec.id EXPENSE_CATEGORY_ID,
                        ec.name EXPENSE_CATEGORY_NAME,
                        ec.type EXPENSE_CATEGORY_TYPE,
                        ec.created_at EXPENSE_CATEGORY_CREATED_AT,
                        ec.color EXPENSE_CATEGORY_COLOR,
                        ec.icon EXPENSE_CATEGORY_ICON
                    FROM T_FIN_EXPENSE e 
                    INNER JOIN T_FIN_EXPENSE_CATEGORY ec ON ec.id = e.category_id
                    WHERE e.account_id = ?   
                    AND "DATE" BETWEEN ? AND ?
                """;

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setLong(1, account.getId());
                stm.setDate(2, java.sql.Date.valueOf(start));
                stm.setDate(3, java.sql.Date.valueOf(end));

                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    expenses.add(fromResultSet(rs));
                }

                return expenses;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Expense update(Expense expense) {
        String sql = """
            UPDATE T_FIN_EXPENSE SET AMOUNT = ?, "DATE" = ?, DESCRIPTION = ?, OBSERVATIONS = ?, ACCOUNT_ID = ?, CATEGORY_ID = ?
            WHERE ID = ?
        """;

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setDouble(1, expense.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(expense.getDate()));
                stm.setString(3, expense.getDescription());
                stm.setString(4, expense.getObservation());
                stm.setLong(5, expense.getOriginAccountId());
                stm.setLong(6, expense.getCategory().getId());

                stm.executeUpdate();
            }

            return findById(expense.getId());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Expense expense) {
        String sql = "DELETE FROM T_FIN_EXPENSE WHERE ID = ?";

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
        String sql = "SELECT * FROM T_FIN_EXPENSE WHERE ID = ?";

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

    private Expense fromResultSet(ResultSet result) throws SQLException {
        return new Expense(
                result.getLong("ID"),
                result.getDouble("AMOUNT"),
                result.getDate("DATE").toLocalDate(),
                result.getString("DESCRIPTION"),
                result.getString("OBSERVATION"),
                result.getTimestamp("CREATED_AT").toLocalDateTime(),
                new ExpenseCategory(
                        result.getLong("EXPENSE_CATEGORY_ID"),
                        result.getString("EXPENSE_CATEGORY_NAME"),
                        ExpenseCategoryType.valueOf(result.getString("EXPENSE_CATEGORY_TYPE")),
                        result.getTimestamp("EXPENSE_CATEGORY_CREATED_AT"),
                        result.getString("EXPENSE_CATEGORY_COLOR"),
                        result.getString("EXPENSE_CATEGORY_ICON")
                ),
                result.getLong("ACCOUNT_ID")
        );
    }


}
