package dal.conection.pool.impl;

import dal.conection.ConnectionAdapeter;
import dal.conection.ConnectionAdapterImpl;
import dal.conection.pool.WrappedTransactionalConnectionPool;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.InjectProperty;
import infrastructure.anotation.Singleton;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@Singleton
public class WrappedTransactionalConnectionPoolImpl implements WrappedTransactionalConnectionPool {
    private static Logger log = LogManager.getLogger(WrappedTransactionalConnectionPoolImpl.class);

    @InjectProperty
    private String dbUrl;
    @InjectProperty
    private String dbUser;
    @InjectProperty
    private String dbPassword;
    @InjectProperty
    private String dbDriver;
    @InjectProperty
    private String dbMinIdle;
    @InjectProperty
    private String dbMaxIdle;
    @InjectProperty
    private String dbInitialSize;
    @InjectProperty("db.maxOpenStatement")
    private String dbMaxOpenStatement;
    @InjectByType
    private BasicDataSource ds;

    @PostConstruct
    public void init() {
        log.debug("created");

        ds.setUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword((dbPassword));
        ds.setDriverClassName(dbDriver);
        ds.setMinIdle(Integer.parseInt(dbMinIdle));
        ds.setMaxIdle(Integer.parseInt(dbMaxIdle));
        ds.setInitialSize(Integer.parseInt(dbInitialSize));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(dbMaxOpenStatement));
    }

    @Override
    public ConnectionAdapeter getConnectionAdapter() throws SQLException {
        return new ConnectionAdapterImpl(ds.getConnection());
    }

    @Override
    public ConnectionAdapeter getConnectionAdapterPreparedForTransaction() throws SQLException {
        ConnectionAdapeter toReturn = new ConnectionAdapterImpl(ds.getConnection());
        toReturn.setAutoCommit(false);
        toReturn.setIsTransaction(true);
        return toReturn;
    }
}
