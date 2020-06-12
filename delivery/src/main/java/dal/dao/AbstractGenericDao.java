package dal.dao;

import dal.dao.maper.EntityToPreparedStatmentMapper;
import dal.dao.maper.ResultSetToEntityMapper;

import java.util.List;
import java.util.Optional;

public interface AbstractGenericDao<E> {
    List<E> findAllByLongParam(long param, String query, ResultSetToEntityMapper<E> mapper);

    List<E> findAllByLongParamPageable(long param, Integer offset, Integer limit, String query, ResultSetToEntityMapper<E> mapper);

    long countAllByLongParam(long param, String query);

    boolean save(E entity, String saveQuery, EntityToPreparedStatmentMapper<E> mapper);

    Optional<E> findById(long id, String query, ResultSetToEntityMapper<E> mapper);

    boolean deleteById(long id, String query, ResultSetToEntityMapper<E> mapper);

    List<E> findAll(String query, ResultSetToEntityMapper<E> mapper);

    boolean update(E entity, String saveQuery, EntityToPreparedStatmentMapper<E> mapper);
}
