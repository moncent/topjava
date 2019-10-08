package ru.javawebinar.topjava.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    void create(T obj);
    Optional<T> find(Integer id);
    List<T> findAll();
    void update(T obj);
    void delete(Integer id);
}
