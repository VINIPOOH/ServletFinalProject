package dal.dao.impl;

import dal.dao.UserDao;
import dal.handling.conection.ConnectionWithRestrictedAbilities;
import dal.handling.conection.pool.DbConnectionPoolHolder;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.RoleType;
import dal.entity.User;
import dal.exeptions.DBRuntimeException;
import exeptions.NoSuchUserException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static dal.dao.UserDaoConstants.USER_FIND_BY_EMAIL;
import static dal.dao.UserDaoConstants.USER_REPLENISH_BALANCE;


public class JDBCUserDao extends JDBCAbstractGenericDao<User> implements UserDao {

    private final String USER_SAVE = "user.save";
    private final String GET_USER_BALANCE_IF_ENOGFE_MONEY =
            "user.get.user.bulance.if.enought.money";


    public JDBCUserDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
    }

    public Optional<User> findByEmailAndPasswordWithPermissions(String email, String password) {
        ResultSetToEntityMapper<User> mapper=resultSet -> Optional.of(User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .accountNonExpired(resultSet.getBoolean("account_non_expired"))
                .accountNonLocked(resultSet.getBoolean("account_non_locked"))
                .credentialsNonExpired(resultSet.getBoolean("credentials_non_expired"))
                .enabled(resultSet.getBoolean("enabled"))
                .userMoneyInCents(resultSet.getLong("user_money_in_cents"))
                .roleType(RoleType.valueOf(resultSet.getString("role")))
                .build());

        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_FIND_BY_EMAIL))) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            Optional<User> user;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                user = resultSet.next() ? mapper.map(resultSet) : Optional.empty();
            }
            return user;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public void replenishUserBalance(long userId, long money) throws NoSuchUserException {
        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
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
    public boolean save(User entity) throws SQLException {
        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_SAVE))) {
            preparedStatement.setString(1, entity.getEmail());
            preparedStatement.setString(2, entity.getPassword());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    public boolean replenishUserBalenceOnSumeIfItPosible(long userId, long sumWhichUserNeed) throws SQLException {
        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_USER_BALANCE_IF_ENOGFE_MONEY))) {
            preparedStatement.setLong(1, sumWhichUserNeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, sumWhichUserNeed);
            return preparedStatement.executeUpdate() > 0;
        }
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
