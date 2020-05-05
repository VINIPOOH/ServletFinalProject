package dal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDao<E, ID> {


    Optional<E> findById(ID id);

    List<E> findAll();
}
