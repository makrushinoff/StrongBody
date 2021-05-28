package ua.strongBody.dao;

import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GeneralDAO<T> {

    List<T> findAll() throws DataAccessException;

    void save(T t) throws DataAccessException;

    void updateById(UUID id, T t);

    void deleteById(UUID id);

    Optional<T> findById(UUID id);

}
