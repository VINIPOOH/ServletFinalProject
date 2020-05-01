package dal;

import dal.DaoFactory;
import dal.conection.DbConnectionPoolHolder;
import dal.conection.impl.DbConnectorPoolHolderBasicDataSource;
import dal.dao.*;
import dal.dao.impl.*;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.dao.maper.UserResultToEntityMapper;
import entity.User;

import java.util.ResourceBundle;

import static dal.dao.UserDaoConstants.PATH_TO_PROPERTY_FILE;

public class JDBCDaoHolder implements DaoFactory {
    private static DbConnectionPoolHolder dbConnectorPoolHolder = DbConnectorPoolHolderBasicDataSource.getDbConnectionPoolHolder();
    //    private static UserEntityToPreparedStatmentMapper userMapper = new UserEntityToPreparedStatmentMapper();
    private static ResourceBundle requestsBundle = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);

    private static ResultSetToEntityMapper<User> userResultSetToEntityMapper = new UserResultToEntityMapper();

    private static UserDao userDao = new JDBCUserDao(requestsBundle, dbConnectorPoolHolder, userResultSetToEntityMapper);
    private static LocalityDao localityDao = new LocalityDao(requestsBundle, dbConnectorPoolHolder);
    private static WayDao wayDao = new JDBCWayDao(requestsBundle, dbConnectorPoolHolder);
    private static DeliveryDao deliveryDao = new JDBCDeliveryDao(requestsBundle,dbConnectorPoolHolder);
    private static BillDao billDao = new JDBCBillDao(requestsBundle,dbConnectorPoolHolder);

    public static UserDao getUserDao() {
        return userDao;
    }

    public static LocalityDao getLocalityDao() {
        return localityDao;
    }

    public static WayDao getWayDao() {
        return wayDao;
    }

    public static DeliveryDao getDeliveryDao(){return deliveryDao;}

    public static BillDao getBillDao(){return billDao;}
}
