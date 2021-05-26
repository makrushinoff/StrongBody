package ua.strongBody.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GeneralDAO<T> {

    List<T> findAll();

    void save(T t);

    void updateById(UUID id, T t);

    void deleteById(UUID id);

    Optional<T> findById(UUID id);

}
