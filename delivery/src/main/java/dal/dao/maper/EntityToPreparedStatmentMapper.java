package dal.dao.maper;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface EntityToPreparedStatmentMapper<E> {
    void map(E entity, PreparedStatement preparedStatement);

}
