package dal.control;

import dal.control.conection.pool.TransactionalManager;
import dal.control.conection.pool.impl.TransactionalManagerImpl;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.dao.WayDao;
import dal.dao.impl.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;


public class JDBCDaoContext {
    private static Logger log = LogManager.getLogger(JDBCDaoContext.class);

    private static final String PATH_TO_PROPERTY_FILE = "db-request";
    private static TransactionalManager dbConnectorPoolHolder = TransactionalManagerImpl.getDbConnectionPoolHolder();

    private static ResourceBundle requestsBundle = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);


    private static UserDao userDao = new JDBCUserDao(requestsBundle, dbConnectorPoolHolder);
    private static LocalityDao localityDao = new LocalityDao(requestsBundle, dbConnectorPoolHolder);
    private static WayDao wayDao = new JDBCWayDao(requestsBundle, dbConnectorPoolHolder);
    private static DeliveryDao deliveryDao = new JDBCDeliveryDao(requestsBundle, dbConnectorPoolHolder);
    private static BillDao billDao = new JDBCBillDao(requestsBundle, dbConnectorPoolHolder);

    private JDBCDaoContext() {
        log.debug("created");
    }

    public static UserDao getUserDao() {
        log.debug("getUserDao");
        return userDao;
    }

    public static LocalityDao getLocalityDao() {
        log.debug("getLocalityDao");
        return localityDao;
    }

    public static WayDao getWayDao() {
        log.debug("getWayDao");
        return wayDao;
    }

    public static DeliveryDao getDeliveryDao() {
        log.debug("getDeliveryDao");
        return deliveryDao;
    }

    public static BillDao getBillDao() {
        log.debug("getBillDao");
        return billDao;
    }

    public static TransactionalManager getTransactionManager() {
        log.debug("getTransactionManager");
        return dbConnectorPoolHolder;
    }


}
