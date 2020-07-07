package dal.conection.pool;

import dal.conection.ConnectionAdapter;

import java.sql.SQLException;

/**
 * The database connection pool which configure connections for transaction work with them.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface TransactionalConnectionPool {
    ConnectionAdapter getConnectionAdapter() throws SQLException;

    ConnectionAdapter getConnectionAdapterPreparedForTransaction() throws SQLException;
}
