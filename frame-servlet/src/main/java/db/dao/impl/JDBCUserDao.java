package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.dao.UserDao;
import db.dao.UserDaoConstants;
import db.dao.maper.EntityToPreparedStatmentMapper;
import entity.User;
import exeptions.DBRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class JDBCUserDao extends JDBCAbstractGenericDao<User> implements UserDao, UserDaoConstants {

    public JDBCUserDao(DbConnectionPoolHolder connector, EntityToPreparedStatmentMapper<User> entityToPreparedStatmentMapper, String saveQuery, String findByIdQuery, String findAllQuery, String updateQuery, String deleteQuery, ResourceBundle resourceBundleRequests) {
        super(connector, entityToPreparedStatmentMapper, saveQuery, findByIdQuery, findAllQuery, updateQuery, deleteQuery, resourceBundleRequests);
    }

    @Override
    public Optional<User> findByEmailAndPasswordWithPermissions(String email, String password) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_FIND_BY_EMAIL))) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<User> user = resultSet.next() ? entityToPreparedStatmentMapper.mapResultSetToEntity(resultSet) : Optional.empty();
            return user;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
