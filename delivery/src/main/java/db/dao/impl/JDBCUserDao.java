package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.dao.UserDao;
import db.dao.maper.ResultSetToEntityMapper;
import entity.User;
import exeptions.DBRuntimeException;
import exeptions.NoSuchUserException;
import service.UserService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static db.dao.UserDaoConstants.USER_FIND_BY_EMAIL;
import static db.dao.UserDaoConstants.USER_REPLENISH_BALANCE;


public class JDBCUserDao extends JDBCAbstractGenericDao<User> implements UserDao {


    private final ResultSetToEntityMapper<User> mapResultSetToEntity;

    public JDBCUserDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector, ResultSetToEntityMapper<User> mapResultSetToEntity) {
        super(resourceBundleRequests, connector);
        this.mapResultSetToEntity = mapResultSetToEntity;
    }

    public Optional<User> findByEmailAndPasswordWithPermissions(String email, String password) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_FIND_BY_EMAIL))) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<User> user = resultSet.next() ? mapResultSetToEntity.map(resultSet) : Optional.empty();
            return user;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public void replenishUserBalance(long userId, long money) throws NoSuchUserException {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_REPLENISH_BALANCE))) {
            preparedStatement.setLong(1, money);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new NoSuchUserException();
        }
    }

    @Override
    public User save(User entity) throws SQLException {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
