package br.com.fiap.dao;

import br.com.fiap.exceptions.DatabaseException;
import br.com.fiap.exceptions.EntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.User;
import br.com.fiap.service.AuthService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements BaseDao <User, Long>{
    String sql;
    @Override
    public void insert(User user){
        sql = "INSERT INTO T_FIN_USER (name, cpf, password, username) ?,?,?,?";
        try(Connection conn = ConnectionFactory.getConnection()) {
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getCpf());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getPassword());
                stmt.executeUpdate();
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }
    }
    @Override
    public void update(User user)  {
        sql = "UPDATE T_FIN_USER SET NAME = ?, CPF = ?, PASSWORD = ?, USERNAME = ?";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getCpf());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getUsername());
            }
        } catch (SQLException e){
            throw  new DatabaseException(e);
        }
    }
    @Override
    public void delete(User user) {
        sql = "DELETE T_FIN_USER WHERE ID = ?";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setLong(1, user.getId());
                int rowCount = stmt.executeUpdate();
                if(rowCount == 0){
                    throw new EntityNotFoundException(user.getId());
                }
            }
        }catch(SQLException e){
            throw new DatabaseException(e);
        }
    }
    @Override
    public User findById(Long id) {
        sql = "SELECT * FROM T_FIN_USER WHERE ID = ?";
        try(Connection conn  = ConnectionFactory.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                ResultSet resultSet = stmt.executeQuery();
                if(!resultSet.next()){
                    throw new EntityNotFoundException(id);
                }
                return fromResultSet(resultSet);
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }
    }
    @Override
    public List <User> findAll() {
        List <User>  users = new ArrayList<>();
        sql = "SELECT * FROM T_FIN_USER";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                ResultSet result = stmt.executeQuery();
               while(!result.next()){
                   users.add(fromResultSet(result));
               }
               return users;
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }
    }
    private User fromResultSet(ResultSet result) throws SQLException{

        return new User(
                result.getLong("ID"),
                result.getString("NAME"),
                result.getString("CPF"),

                result.getString("PASSWORD"),
                result.getString("USERNAME")
        );
    }
}

