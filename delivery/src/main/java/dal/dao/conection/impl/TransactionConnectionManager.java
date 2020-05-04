package dal.dao.conection.impl;

import dal.JDBCDaoSingleton;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.dao.conection.ConnectionForTransactions;
import dal.dao.conection.ConnectionWithRestrictedAbilities;
import dal.dao.conection.DbConnectionPoolHolder;
import dal.dao.conection.TransactionManager;
import dal.dao.impl.JDBCBillDao;
import dal.dao.impl.JDBCDeliveryDao;
import dal.dao.impl.JDBCUserDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.dao.maper.UserResultToEntityMapper;
import dal.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static dal.dao.UserDaoConstants.PATH_TO_PROPERTY_FILE;

public class TransactionConnectionManager implements TransactionManager {
    private static ResultSetToEntityMapper<User> userResultSetToEntityMapper = new UserResultToEntityMapper();

    private static ResourceBundle requestsBundle = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);

    private final DbConnectionPoolHolder dbConnectionPoolHolder;
    private final ConnectionTransactionalProxy connection;

    public TransactionConnectionManager(Connection connection) throws SQLException {
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
        return  new JDBCUserDao(requestsBundle, dbConnectionPoolHolder, userResultSetToEntityMapper);
    }

    @Override
    public DeliveryDao getDeliveryDao() {
        return  new JDBCDeliveryDao(requestsBundle, dbConnectionPoolHolder);
    }

    @Override
    public ConnectionWithRestrictedAbilities getConnection() {
        return connection;
    }

    @Override
    public Connection getPureConnection() {
        return null;
    }

    @Override
    public void close() throws SQLException {
        connection.setReadyToClose(true);
        connection.close();
    }
}
