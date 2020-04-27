package db.conection.impl;

import db.conection.DbConnectionPoolHolder;
import exeptions.DBRuntimeException;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static db.conection.DbConnectionConstants.*;

public class DbConnectorPoolHolderBasicDataSource implements DbConnectionPoolHolder {

    private static DbConnectorPoolHolderBasicDataSource dbConnectorPoolHolderBasicDataSource = new DbConnectorPoolHolderBasicDataSource();
    private final BasicDataSource dataSource;

    public DbConnectorPoolHolderBasicDataSource() {
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

    public static DbConnectionPoolHolder getDbConnectionPoolHolder() {
        return dbConnectorPoolHolderBasicDataSource;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }
    }
}
