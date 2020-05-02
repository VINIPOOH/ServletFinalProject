package dal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDao<E, ID> {

    boolean save(E entity) throws SQLException;

    Optional<E> findById(ID id);
//
//    List<E> findAll(Integer offset, Integer limit);
//
    List<E> findAll();
//
//    boolean update(E dal.entity);
//
//    boolean deleteById(ID id);
}