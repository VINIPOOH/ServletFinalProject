package dal.handling;

import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.dao.WayDao;
import dal.dao.impl.*;
import dal.handling.conection.pool.TransactionalManager;
import dal.handling.conection.pool.impl.TransactionalManagerImpl;

import java.sql.SQLException;
import java.util.ResourceBundle;

import static dal.dao.UserDaoConstants.PATH_TO_PROPERTY_FILE;

public class JDBCDaoSingleton {
    private static TransactionalManager dbConnectorPoolHolder = TransactionalManagerImpl.getDbConnectionPoolHolder();

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

    public static TransactionalManager getTransactionManager() throws SQLException {
        return dbConnectorPoolHolder;
    }


}
