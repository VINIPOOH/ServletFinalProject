package dal.control;

import dal.control.conection.pool.TransactionalManager;
import dal.control.conection.pool.impl.TransactionalManagerImpl;
import dal.dao.*;
import dal.dao.impl.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class JDBCDaoContext {
    private static final String PATH_TO_PROPERTY_FILE = "db-request";
    private static Logger log = LogManager.getLogger(JDBCDaoContext.class);
    private static TransactionalManager dbConnectorPoolHolder = TransactionalManagerImpl.getDbConnectionPoolHolder();

    private static ResourceBundle requestsBundle = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);


    private static UserDao userDao = new JDBCUserDao(requestsBundle, dbConnectorPoolHolder);
    private static JDBCLocalityDao localityDao = new JDBCLocalityDao(requestsBundle, dbConnectorPoolHolder);
    private static WayDao wayDao = new JDBCWayDao(requestsBundle, dbConnectorPoolHolder);
    private static DeliveryDao deliveryDao = new JDBCDeliveryDao(requestsBundle, dbConnectorPoolHolder);
    private static BillDao billDao = new JDBCBillDao(requestsBundle, dbConnectorPoolHolder);

    private static Map<Class, Object> contextMap;

    private JDBCDaoContext() {
        log.debug("created");
    }

    static {
        contextMap = new HashMap<>();
        contextMap.put(UserDao.class, userDao);
        contextMap.put(LocalityDao.class, localityDao);
        contextMap.put(WayDao.class, wayDao);
        contextMap.put(DeliveryDao.class, deliveryDao);
        contextMap.put(BillDao.class, billDao);
        contextMap.put(TransactionalManager.class, dbConnectorPoolHolder);

    }

    public static <T> T getObject(Class<T> classType) {
        return (T) contextMap.get(classType);
    }


    public static TransactionalManager getTransactionManager() {
        log.debug("getTransactionManager");
        return dbConnectorPoolHolder;
    }


}
