package br.com.fiap.dao;

import java.sql.ResultSet;
import java.util.List;

public interface BaseDao<T, ID> {
    T insert(T entity);
    void update(T entity);
    void delete(T entity);
    T findById(ID id);
    List<T> findAll();
}
