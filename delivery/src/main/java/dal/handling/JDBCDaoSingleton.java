package dal.handling;

import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.dao.WayDao;
import dal.handling.conection.pool.DbConnectionPoolHolder;
import dal.handling.transaction.TransactionManager;
import dal.handling.conection.pool.impl.DbConnectorPoolHolderBasicDataSource;
import dal.handling.transaction.impl.TransactionManagerImpl;
import dal.dao.impl.*;

import java.sql.SQLException;
import java.util.ResourceBundle;

import static dal.dao.UserDaoConstants.PATH_TO_PROPERTY_FILE;

public class JDBCDaoSingleton {
    private static DbConnectionPoolHolder dbConnectorPoolHolder = DbConnectorPoolHolderBasicDataSource.getDbConnectionPoolHolder();

    private static ResourceBundle requestsBundle = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);


    private static UserDao userDao = new JDBCUserDao(requestsBundle, dbConnectorPoolHolder);
    private static LocalityDao localityDao = new LocalityDao(requestsBundle, dbConnectorPoolHolder);
    private static WayDao wayDao = new JDBCWayDao(requestsBundle, dbConnectorPoolHolder);
    private static DeliveryDao deliveryDao = new JDBCDeliveryDao(requestsBundle, dbConnectorPoolHolder);
    private static BillDao billDao = new JDBCBillDao(requestsBundle, dbConnectorPoolHolder);

    private JDBCDaoSingleton() {
    }

    public static UserDao getUserDao() {
        return userDao;
    }

    public static LocalityDao getLocalityDao() {
        return localityDao;
    }

    public static WayDao getWayDao() {
        return wayDao;
    }

    public static DeliveryDao getDeliveryDao() {
        return deliveryDao;
    }

    public static BillDao getBillDao() {
        return billDao;
    }

    public static TransactionManager getTransactionManager() throws SQLException {
        return new TransactionManagerImpl(dbConnectorPoolHolder.getConnection().getSubject());
    }

    public static BillDao getBillDaoForTransaction(TransactionManager dbConnectorPoolHolder){
        return new JDBCBillDao(requestsBundle, dbConnectorPoolHolder);
    }

    public static WayDao getWayDaoForTransaction(TransactionManager dbConnectorPoolHolder){
        return new JDBCWayDao(requestsBundle, dbConnectorPoolHolder);
    }

    public static UserDao getUserDaoForTransaction(TransactionManager dbConnectorPoolHolder){
        return new JDBCUserDao(requestsBundle, dbConnectorPoolHolder);
    }

    public static DeliveryDao getDeliveryForTransaction(TransactionManager dbConnectorPoolHolder){
        return new JDBCDeliveryDao(requestsBundle, dbConnectorPoolHolder);
    }


}
