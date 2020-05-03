package dal;

import dal.dao.conection.DbConnectionPoolHolder;
import dal.dao.conection.impl.DbConnectorPoolHolderBasicDataSource;
import dal.dao.*;
import dal.dao.conection.impl.TransactionalConnectionHolder;
import dal.dao.impl.*;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.dao.maper.UserResultToEntityMapper;
import dal.entity.Bill;
import dal.entity.User;

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

    public static TransactionalConnectionHolder getConnectionPullHolderForTransaction(){
        return new TransactionalConnectionHolder(dbConnectorPoolHolder.getConnection());
    }

    public static UserDao getTransactionalUserDao(DbConnectionPoolHolder poolHolder){
        return new JDBCUserDao(requestsBundle, poolHolder, userResultSetToEntityMapper);
    }

    public static WayDao getTransactionalWayDao(DbConnectionPoolHolder poolHolder){
        return new JDBCWayDao(requestsBundle, poolHolder);
    }

    public static DeliveryDao getTransactionalDeliveryDao(DbConnectionPoolHolder poolHolder){
        return new JDBCDeliveryDao(requestsBundle, poolHolder);
    }

    public static BillDao getTransactionalBillDao(DbConnectionPoolHolder poolHolder){
        return new JDBCBillDao(requestsBundle, poolHolder);
    }


}
