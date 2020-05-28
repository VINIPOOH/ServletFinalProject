package dal.conection.pool;

import dal.conection.ConnectionAdapeter;

import java.sql.SQLException;

public interface ConnectionManager extends AutoCloseable {
    ConnectionAdapeter getConnection() throws SQLException;

    void startTransaction() throws SQLException;

    void commit() throws SQLException;

    void rollBack() throws SQLException;

    @Override
    void close();
}
