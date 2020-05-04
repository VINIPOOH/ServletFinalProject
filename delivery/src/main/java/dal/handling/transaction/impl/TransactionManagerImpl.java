package dal.handling.transaction.impl;

import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.handling.conection.impl.ConnectionTransactionalProxy;
import dal.handling.conection.pool.DbConnectionPoolHolder;
import dal.handling.transaction.TransactionManager;
import dal.handling.conection.pool.impl.DbTransactionPoolHolder;
import dal.dao.impl.JDBCBillDao;
import dal.dao.impl.JDBCDeliveryDao;
import dal.dao.impl.JDBCUserDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static dal.dao.UserDaoConstants.PATH_TO_PROPERTY_FILE;

public class TransactionManagerImpl implements TransactionManager {

    private static ResourceBundle requestsBundle = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);

    private final DbConnectionPoolHolder dbConnectionPoolHolder;
    private final ConnectionTransactionalProxy connection;

    public TransactionManagerImpl(Connection connection) throws SQLException {
        this.connection = new ConnectionTransactionalProxy(connection);
        this.dbConnectionPoolHolder=new DbTransactionPoolHolder(new ConnectionTransactionalProxy(connection));
    }

    @Override
    public void rollBack() throws SQLException {
        connection.rollBack();
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public BillDao getBillDao() {
        return  new JDBCBillDao(requestsBundle, dbConnectionPoolHolder);
    }

    @Override
    public UserDao getUserDao() {
        return  new JDBCUserDao(requestsBundle, dbConnectionPoolHolder);
    }

    @Override
    public DeliveryDao getDeliveryDao() {
        return  new JDBCDeliveryDao(requestsBundle, dbConnectionPoolHolder);
    }

    @Override
    public void close() throws SQLException {
        connection.setReadyToClose(true);
        connection.close();
    }
}
