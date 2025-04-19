package br.com.fiap.dao;

import br.com.fiap.model.User;
import java.util.List;

public interface BaseDao<T, ID> {
    T insert(T entity);
    T update(T entity);
    void delete(T entity);
    T findById(ID id);
    List<T> findAll();
}
