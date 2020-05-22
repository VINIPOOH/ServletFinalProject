package dal.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E, ID> {

    boolean save(E entity);

    Optional<E> findById(ID id);

    List<E> findAll(Integer offset, Integer limit);

    boolean update(E entity);

    boolean deleteById(ID id);

    Integer countAll();
}
