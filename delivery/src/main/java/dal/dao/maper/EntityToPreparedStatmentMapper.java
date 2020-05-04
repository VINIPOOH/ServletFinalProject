package dal.dao.maper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface EntityToPreparedStatmentMapper<E> {
    void insertStatementMapper(E entity, PreparedStatement preparedStatement) throws SQLException;

    void updateStatementMapper(E entity, PreparedStatement preparedStatement) throws SQLException;
}
