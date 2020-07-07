package dal.conection.pool.impl;

import dal.conection.ConnectionAdapter;
import dal.conection.ConnectionAdapterImpl;
import dal.conection.pool.TransactionalConnectionPool;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.InjectStringProperty;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

/**
 * The database connection pool which configure connections for transaction work with them.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class TransactionalConnectionPoolImpl implements TransactionalConnectionPool {
    private static final Logger log = LogManager.getLogger(TransactionalConnectionPoolImpl.class);

    @InjectStringProperty
    private String dbUrl;
    @InjectStringProperty
    private String dbUser;
    @InjectStringProperty
    private String dbPassword;
    @InjectStringProperty
    private String dbDriver;
    @InjectStringProperty
    private String dbMinIdle;
    @InjectStringProperty
    private String dbMaxIdle;
    @InjectStringProperty
    private String dbInitialSize;
    @InjectStringProperty("db.maxOpenStatement")
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
    public ConnectionAdapter getConnectionAdapter() throws SQLException {
        return new ConnectionAdapterImpl(ds.getConnection());
    }

    @Override
    public ConnectionAdapter getConnectionAdapterPreparedForTransaction() throws SQLException {
        ConnectionAdapter toReturn = new ConnectionAdapterImpl(ds.getConnection());
        toReturn.setAutoCommit(false);
        toReturn.setIsTransaction(true);
        return toReturn;
    }
}
