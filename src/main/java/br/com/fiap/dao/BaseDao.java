package br.com.fiap.dao;

import br.com.fiap.model.User;
import java.util.List;

public interface BaseDao<T, ID> {
    User insert(T entity);
    void update(T entity);
    void delete(T entity);
    T findById(ID id);
    List<T> findAll();
}
