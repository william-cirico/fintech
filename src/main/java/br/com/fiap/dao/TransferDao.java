package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Account;
import br.com.fiap.model.Expense;
import br.com.fiap.model.TransactionType;
import br.com.fiap.model.Transfer;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransferDao implements BaseDao<Transfer, Long> {
    @Override

    public List<Transfer> findAll() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = """
            SELECT
                t.*,
                o.id origin_account_id,
                o.name origin_account_name,
                o.balance origin_account_balance,
                o.created_at origin_account_created_at,
                o.user_id origin_account_user_id,
                d.id origin_account_id,
                d.name destination_account_name,
                d.balance destination_account_balance,
                d.created_at destination_account_created_at,
                d.user_id destination_account_user_id
            FROM T_FIN_TRANSFER t
            INNER JOIN T_FIN_ACCOUNT o ON o.id = t.origin_account_id
            INNER JOIN T_FIN_ACCOUNT d ON d.id = t.destination_account_id
        """;

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                ResultSet result = stm.executeQuery();
                while(result.next()){
                    transfers.add(fromResultSet(result));
                }
            }
            return transfers;

        } catch (SQLException e){
            throw new DatabaseException(e);
        }

    }

    @Override
    public Transfer insert(Transfer transfer) {
        String sql = "INSERT INTO T_FIN_TRANSFER + " +
                "(AMOUNT, DATE, DESCRIPTION, OBSERVATION, ORIGIN_ACCOUNT_ID, DESTINATION_ACCOUNT_ID)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = ConnectionFactory.getConnection()) {
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, transfer.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(transfer.getDate()));
                stm.setString(3, transfer.getDescription());
                stm.setString(4, transfer.getObservations());
                stm.setLong(5, transfer.getOriginAccountId());
                stm.setLong(6, transfer.getDestinationAccountId());

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

    @Override
    public Transfer update(Transfer transfer) {
        String sql = "UPDATE T_FIN_TRANSFER SET" +
                "AMOUNT = ?, DATE = ?, DESCRIPTION = ?, OBSERVATIONS = ?, CREATED_AT = ?, TYPE = ?, ORIGIN_ACCOUNT = ?, DESTINATION_ACCOUNT = ?" +
                "WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setDouble(1, transfer.getAmount());
                stm.setDate(2, java.sql.Date.valueOf(transfer.getDate()));
                stm.setString(3, transfer.getDescription());
                stm.setString(4, transfer.getObservations());
                stm.setLong(5, transfer.getOriginAccountId());
                stm.setLong(6, transfer.getDestinationAccountId());
                stm.setLong(7, transfer.getId());

                stm.executeUpdate();
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }

        return findById(transfer.getId());


    }

    @Override
    public void delete(Transfer transfer) {
        String sql = "DELETE T_FIN_TRANSFER WHERE ID = ?";

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, transfer.getId());
                int rowCount = stm.executeUpdate();
                if(rowCount == 0) {
                    throw new EntityNotFoundException(transfer.getId());
                }
            }
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }

    }

    @Override
    public Transfer findById(Long id) {
        String sql = """
            SELECT
                t.*,
                o.id origin_account_id,
                o.name origin_account_name,
                o.balance origin_account_balance,
                o.created_at origin_account_created_at,
                o.user_id origin_account_user_id,
                d.id origin_account_id,
                d.name destination_account_name,
                d.balance destination_account_balance,
                d.created_at destination_account_created_at,
                d.user_id destination_account_user_id
            FROM T_FIN_TRANSFER t
            INNER JOIN T_FIN_ACCOUNT o ON o.id = t.origin_account_id
            INNER JOIN T_FIN_ACCOUNT d ON d.id = t.destination_account_id
        """;

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, id);
                ResultSet result = stm.executeQuery();
            return fromResultSet(result);
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }
    }

    public List<Transfer> findAllByPeriodFromAccount(Account account, LocalDate start, LocalDate end) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = """
            SELECT * FROM T_FIN_TRANSFER            
            WHERE account_id = ?            
            AND e.date BETWEEN ? AND ?
        """;

        try(Connection connection = ConnectionFactory.getConnection()){
            try(PreparedStatement stm = connection.prepareStatement(sql)){
                stm.setLong(1, account.getId());
                stm.setDate(2, java.sql.Date.valueOf(start));
                stm.setDate(3, java.sql.Date.valueOf(end));

                ResultSet rs = stm.executeQuery();
                while(rs.next()){
                    transfers.add(fromResultSet(rs));
                }

                return transfers;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private Transfer fromResultSet(ResultSet result) throws SQLException {
        return new Transfer(
                result.getLong("ID"),
                result.getDouble("AMOUNT"),
                result.getDate("DATE").toLocalDate(),
                result.getString("DESCRIPTION"),
                result.getString("OBSERVATIONS"),
                result.getTimestamp("CREATED_AT").toLocalDateTime(),
                new Account(
                    result.getLong("ORIGIN_ACCOUNT_ID"),
                    result.getString("ORIGIN_ACCOUNT_NAME"),
                    result.getDouble("ORIGIN_ACCOUNT_BALANCE"),
                    result.getTimestamp("ORIGIN_ACCOUNT_CREATED_AT").toLocalDateTime(),
                    result.getLong("ORIGIN_ACCOUNT_USER_ID")
                        ),
                new Account(
                    result.getLong("DESTINATION_ACCOUNT_ID"),
                    result.getString("DESTINATION_ACCOUNT_NAME"),
                    result.getDouble("DESTINATION_ACCOUNT_BALANCE"),
                    result.getTimestamp("DESTINATION_ACCOUNT_CREATED_AT").toLocalDateTime(),
                    result.getLong("DESTINATION_ACCOUNT_USER_ID")
                    )
        );
    }



}
