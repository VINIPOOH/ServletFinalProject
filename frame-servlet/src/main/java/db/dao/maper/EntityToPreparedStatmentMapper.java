package db.dao.maper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface EntityToPreparedStatmentMapper<E> {
    void insertStatementMapper(E entity, PreparedStatement preparedStatement) throws SQLException;

    void updateStatementMapper(E entity, PreparedStatement preparedStatement) throws SQLException;
}
