package dal.handling.conection.pool.impl;

import dal.handling.conection.ConnectionAdapeter;
import dal.handling.conection.impl.ConnectionAdapterImpl;
import dal.handling.conection.pool.TransactionalManager;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class TransactionalManagerImpl implements TransactionalManager {

    private static final String RESOURCE_BUNDLE_DATABASE = "database";
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_MIN_IDLE = "db.minIdle";
    private static final String DB_MAX_IDLE = "db.maxIdle";
    private static final String DB_INITIAL_SIZE = "db.initialSize";
    private static final String DB_MAX_OPEN_STATEMENT = "db.maxOpenStatement";

    private static TransactionalManagerImpl transactionalManagerImpl = new TransactionalManagerImpl();
    private final BasicDataSource dataSource;
    private ThreadLocal<ConnectionAdapeter> connectionThreadLocal = new ThreadLocal<>();


    public TransactionalManagerImpl() {
        ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_DATABASE);
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(bundle.getString(DB_URL));
        ds.setUsername(bundle.getString(DB_USER));
        ds.setPassword(bundle.getString(DB_PASSWORD));
        ds.setDriverClassName(bundle.getString(DB_DRIVER));
        ds.setMinIdle(Integer.parseInt(bundle.getString(DB_MIN_IDLE)));
        ds.setMaxIdle(Integer.parseInt(bundle.getString(DB_MAX_IDLE)));
        ds.setInitialSize(Integer.parseInt(bundle.getString(DB_INITIAL_SIZE)));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(bundle.getString(DB_MAX_OPEN_STATEMENT)));
        dataSource = ds;
    }

    public static TransactionalManager getDbConnectionPoolHolder() {
        return transactionalManagerImpl;
    }


    public ConnectionAdapeter getConnection() throws SQLException {
        ConnectionAdapeter connection = connectionThreadLocal.get();

        if (connection != null) {
            return connection;
        }
        return new ConnectionAdapterImpl(dataSource.getConnection());
    }

    public void startTransaction() throws SQLException {
        ConnectionAdapeter connection = connectionThreadLocal.get();
        if (connection != null) {
            throw new SQLException("Transaction already started");
        }
        connection = getConnection();
        connection.setAutoCommit(false);
        connection.setIsTransaction(true);
        connectionThreadLocal.set(connection);
    }

    public void commit() throws SQLException {
        ConnectionAdapeter connection = connectionThreadLocal.get();

        if (connection == null) {
            throw new SQLException("Transaction not started to be commit.");
        }
        connection.commit();
    }

    public void rollBack() throws SQLException {
        ConnectionAdapeter connection = connectionThreadLocal.get();

        if (connection == null) {
            throw new SQLException("Transaction not started to be rollback.");
        }

        connection.rollBack();
    }

    public void close() throws SQLException {
        ConnectionAdapeter connection = connectionThreadLocal.get();

        if (connection == null) {
            throw new SQLException("Transaction not started to be rollback.");
        }
        connection.setIsTransaction(false);
        connection.close();
        connectionThreadLocal.remove();

    }
}
