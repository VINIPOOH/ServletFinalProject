package dal.dao.maper;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetToEntityMapper<E> {
    E map(ResultSet resultSet) throws SQLException;
}
