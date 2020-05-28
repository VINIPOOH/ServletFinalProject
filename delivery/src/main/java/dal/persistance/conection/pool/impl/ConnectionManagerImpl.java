package dal.persistance.conection.pool.impl;

import dal.persistance.conection.ConnectionAdapeter;
import dal.persistance.conection.ConnectionAdapterImpl;
import dal.persistance.conection.pool.ConnectionManager;
import infrastructure.anotation.InjectProperty;
import infrastructure.anotation.Singleton;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ResourceBundle;

@Singleton
public class ConnectionManagerImpl implements ConnectionManager {
    @InjectProperty("database")
    private String RESOURCE_BUNDLE_DATABASE;
    private String DB_URL = "db.url";
    private String DB_USER = "db.user";
    private String DB_PASSWORD = "db.password";
    private String DB_DRIVER = "db.driver";
    private String DB_MIN_IDLE = "db.minIdle";
    private String DB_MAX_IDLE = "db.maxIdle";
    private String DB_INITIAL_SIZE = "db.initialSize";
    private String DB_MAX_OPEN_STATEMENT = "db.maxOpenStatement";
    private static Logger log = LogManager.getLogger(ConnectionManagerImpl.class);
    private BasicDataSource dataSource;
    private ThreadLocal<ConnectionAdapeter> connectionThreadLocal = new ThreadLocal<>();

    @PostConstruct
    public void init() {
        log.debug("created");

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


    public ConnectionAdapeter getConnection() throws SQLException {
        log.debug("");

        ConnectionAdapeter connection = connectionThreadLocal.get();
        if (connection != null) {
            return connection;
        }
        return new ConnectionAdapterImpl(dataSource.getConnection());
    }

    public void startTransaction() throws SQLException {
        log.debug("startTransaction");

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
}
