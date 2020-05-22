package dal.dao.maper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface EntityToPreparedStatmentMapper<E> {
    void map(E entity, PreparedStatement preparedStatement) throws SQLException;

}
