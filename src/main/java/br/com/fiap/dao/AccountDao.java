package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Account;
import br.com.fiap.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao implements BaseDao<Account, Long> {
    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM T_FIN_ACCOUNT";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    accounts.add(fromResultSet(result));
                }
            }

            return accounts;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

    }

    @Override
    public Account findById(Long id) {
        String sql = "SELECT * FROM T_FIN_ACCOUNT WHERE ID = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                ResultSet resultSet = stmt.executeQuery();
                if (!resultSet.next()) {
                    throw new EntityNotFoundException(id);
                }
                return fromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Account insert(Account account) {
        String sql = "INSERT INTO T_FIN_ACCOUNT (NAME, BALANCE) VALUES (?,?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql);) {
                stmt.setString(1, account.getName());
                stmt.setDouble(2, account.getBalance());
                stmt.executeUpdate();
                return (findById(account.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Account update(Account account) {
        String sql = "UPDATE T_FIN_ACCOUNT SET NAME = ?, BALANCE  = ? WHERE ID = ? ";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, account.getName());
                stmt.setDouble(2, account.getBalance());
                stmt.setLong(3, account.getId());
                stmt.executeUpdate();
                return (findById(account.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return findById(account.getId());
    }

    @Override
    public void delete(Account account) {
        String sql = "DELETE FROM T_FIN_ACCOUNT WHERE ID = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, account.getId());
                int rowCount = stmt.executeUpdate();
                if (rowCount == 0) {
                    throw new EntityNotFoundException(account.getId());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private Account fromResultSet(ResultSet result) throws SQLException {
        return new Account(
                result.getLong("ID"),
                result.getString("NAME"),
                result.getDouble("BALANCE")
        );
    }
}
