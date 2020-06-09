package dal.conection.pool.impl;

import dal.conection.ConnectionAdapeter;
import dal.conection.pool.TransactionalManager;
import dal.conection.pool.WrappedTransactionalConnectionPool;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;

@Singleton
public class TransactionalManagerImpl implements TransactionalManager {

    private static Logger log = LogManager.getLogger(TransactionalManagerImpl.class);

    @InjectByType
    WrappedTransactionalConnectionPool wrappedTransactionalConnectionPool;

    @InjectByType
    private ThreadLocal<ConnectionAdapeter> connectionThreadLocal;


    public ConnectionAdapeter getConnection() throws SQLException {
        log.debug("");

        ConnectionAdapeter connection = connectionThreadLocal.get();
        if (connection != null) {
            return connection;
        }
        return wrappedTransactionalConnectionPool.getConnectionAdapter();
    }

    public void startTransaction() throws SQLException {
        log.debug("startTransaction");

        ConnectionAdapeter connection = connectionThreadLocal.get();
        if (connection != null) {
            throw new SQLException("Transaction already started");
        }
        connectionThreadLocal.set(wrappedTransactionalConnectionPool.getConnectionAdapterPreparedForTransaction());
    }

    public void commit() throws SQLException {
        log.debug("commit");

        ConnectionAdapeter connection = connectionThreadLocal.get();

        if (connection == null) {
            throw new SQLException("Transaction not started to be commit");
        }
        connection.commit();
    }

    public void rollBack() throws SQLException {
        log.debug("rollBack");

        ConnectionAdapeter connection = connectionThreadLocal.get();

        if (connection == null) {
            throw new SQLException("Transaction not started to be rollback");
        }

        connection.rollBack();
    }

    public void close() {
        log.debug("close");

        ConnectionAdapeter connection = connectionThreadLocal.get();

        if (connection == null) {
            log.error("transaction is already closed");
            return;
        }
        connection.setIsTransaction(false);
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("problems with db");
        }
        connectionThreadLocal.remove();
    }

    @Override
    protected void finalize() {
        this.close();
    }
}
