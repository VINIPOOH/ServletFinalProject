package dal.conection.pool.impl;

import dal.conection.ConnectionAdapeter;
import dal.conection.ConnectionAdapterImpl;
import dal.conection.pool.ConnectionManager;
import infrastructure.anotation.InjectProperty;
import infrastructure.anotation.Singleton;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@Singleton
public class ConnectionManagerImpl implements ConnectionManager {

    @InjectProperty()
    private String dbUrl;
    @InjectProperty()
    private String dbUser;
    @InjectProperty()
    private String dbPassword;
    @InjectProperty()
    private String dbDriver;
    @InjectProperty()
    private String dbMinIdle;
    @InjectProperty()
    private String dbMaxIdle;
    @InjectProperty()
    private String dbInitialSize;
    @InjectProperty("db.maxOpenStatement")
    private String dbMaxOpenStatement;
    private static Logger log = LogManager.getLogger(ConnectionManagerImpl.class);
    private BasicDataSource dataSource;
    private ThreadLocal<ConnectionAdapeter> connectionThreadLocal = new ThreadLocal<>();

    @PostConstruct
    public void init() {
        log.debug("created");

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword((dbPassword));
        ds.setDriverClassName(dbDriver);
        ds.setMinIdle(Integer.parseInt(dbMinIdle));
        ds.setMaxIdle(Integer.parseInt(dbMaxIdle));
        ds.setInitialSize(Integer.parseInt(dbInitialSize));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(dbMaxOpenStatement));
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

    @Override
    protected void finalize() {
        this.close();
    }
}
