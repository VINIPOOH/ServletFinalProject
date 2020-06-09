package dal.conection.pool;

import dal.conection.ConnectionAdapeter;

import java.sql.SQLException;

public interface WrappedTransactionalConnectionPool {
    ConnectionAdapeter getConnectionAdapter() throws SQLException;

    ConnectionAdapeter getConnectionAdapterPreparedForTransaction() throws SQLException;
}
