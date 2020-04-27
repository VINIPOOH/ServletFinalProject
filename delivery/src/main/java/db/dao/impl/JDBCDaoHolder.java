package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.conection.impl.DbConnectorPoolHolderBasicDataSource;
import db.dao.DaoFactory;
import db.dao.UserDao;
import db.dao.maper.ResultSetToEntityMapper;
import db.dao.maper.UserEntityToPreparedStatmentMapper;
import db.dao.maper.UserResultToEntityMapper;
import entity.User;

import java.util.ResourceBundle;

import static db.dao.UserDaoConstants.PATH_TO_PROPERTY_FILE;

public class JDBCDaoHolder implements DaoFactory {
    private static DbConnectionPoolHolder dbConnectorPoolHolder = DbConnectorPoolHolderBasicDataSource.getDbConnectionPoolHolder();
//    private static UserEntityToPreparedStatmentMapper userMapper = new UserEntityToPreparedStatmentMapper();
    private static ResourceBundle RequestsBundle = ResourceBundle.getBundle(PATH_TO_PROPERTY_FILE);

    private static ResultSetToEntityMapper<User> userResultSetToEntityMapper = new UserResultToEntityMapper();
    private static UserDao userDao = new JDBCUserDao(RequestsBundle,dbConnectorPoolHolder, userResultSetToEntityMapper);
//            new JDBCUserDao(dbConnectorPoolHolder, userMapper, resourceBundle.getString(USER_SAVE_QUERY),
//            null, null, null, null, resourceBundle);

    public static UserDao getUserDao() {
        return userDao;
    }
}
