package service;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService<T> {
    void create(T entity) throws SQLException;
    T read(int id) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(int id) throws SQLException;
    List<T> readAll() throws SQLException;
} 