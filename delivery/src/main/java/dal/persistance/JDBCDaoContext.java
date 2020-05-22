package dal.persistance;

import dal.dao.*;
import dal.dao.impl.*;
import dal.persistance.conection.pool.ConnectionManager;
import dal.persistance.conection.pool.impl.ConnectionManagerImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class JDBCDaoContext {
    private static final String PATH_TO_PROPERTY_FILE = "db-request";
    private static final ConnectionManager DB_CONNECTION_POOL_HOLDER = ConnectionManagerImpl.getDbConnectionPoolHolder();
    private static final ResourceBundle REQUESTS_BUNDLE = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);
    private static final UserDao USER_DAO = new JDBCUserDao(REQUESTS_BUNDLE, DB_CONNECTION_POOL_HOLDER);
    private static final LocalityDao LOCALITY_DAO = new JDBCLocalityDao(REQUESTS_BUNDLE, DB_CONNECTION_POOL_HOLDER);
    private static final WayDao WAY_DAO = new JDBCWayDao(REQUESTS_BUNDLE, DB_CONNECTION_POOL_HOLDER);
    private static final DeliveryDao DELIVERY_DAO = new JDBCDeliveryDao(REQUESTS_BUNDLE, DB_CONNECTION_POOL_HOLDER);
    private static final BillDao BILL_DAO = new JDBCBillDao(REQUESTS_BUNDLE, DB_CONNECTION_POOL_HOLDER);
    private static Logger log = LogManager.getLogger(JDBCDaoContext.class);
    private static Map<Class, Object> contextMap;

    static {
        contextMap = new HashMap<>();
        contextMap.put(UserDao.class, USER_DAO);
        contextMap.put(LocalityDao.class, LOCALITY_DAO);
        contextMap.put(WayDao.class, WAY_DAO);
        contextMap.put(DeliveryDao.class, DELIVERY_DAO);
        contextMap.put(BillDao.class, BILL_DAO);
        contextMap.put(ConnectionManager.class, DB_CONNECTION_POOL_HOLDER);

    }

    private JDBCDaoContext() {
        log.debug("created");
    }

    public static <T> T getObject(Class<T> classType) {
        return (T) contextMap.get(classType);
    }


    public static ConnectionManager getTransactionManager() {
        log.debug("getTransactionManager");
        return DB_CONNECTION_POOL_HOLDER;
    }


}
