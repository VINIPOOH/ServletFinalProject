package dal.control.conection.pool;

import dal.control.conection.ConnectionAdapeter;

import java.sql.SQLException;

public interface TransactionalManager extends AutoCloseable {
    ConnectionAdapeter getConnection() throws SQLException;

    void startTransaction() throws SQLException;

    void commit() throws SQLException;

    void rollBack() throws SQLException;

    @Override
    void close() throws SQLException;
}
