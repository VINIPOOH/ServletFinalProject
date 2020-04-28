package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.conection.impl.DbConnectorPoolHolderBasicDataSource;
import db.dao.DaoFactory;
import db.dao.UserDao;
import db.dao.WayDao;
import db.dao.maper.ResultSetToEntityMapper;
import db.dao.maper.UserResultToEntityMapper;
import entity.User;

import java.util.ResourceBundle;

import static db.dao.UserDaoConstants.PATH_TO_PROPERTY_FILE;

public class JDBCDaoHolder implements DaoFactory {
    private static DbConnectionPoolHolder dbConnectorPoolHolder = DbConnectorPoolHolderBasicDataSource.getDbConnectionPoolHolder();
    //    private static UserEntityToPreparedStatmentMapper userMapper = new UserEntityToPreparedStatmentMapper();
    private static ResourceBundle requestsBundle = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);

    private static ResultSetToEntityMapper<User> userResultSetToEntityMapper = new UserResultToEntityMapper();

    private static UserDao userDao = new JDBCUserDao(requestsBundle, dbConnectorPoolHolder, userResultSetToEntityMapper);
    private static LocalityDao localityDao = new LocalityDao(requestsBundle, dbConnectorPoolHolder);
    private static WayDao wayDao = new JDBCWayDao(requestsBundle, dbConnectorPoolHolder);

    public static UserDao getUserDao() {
        return userDao;
    }

    public static LocalityDao getLocalityDao() {
        return localityDao;
    }

    public static WayDao getWayDao() {
        return wayDao;
    }
}
