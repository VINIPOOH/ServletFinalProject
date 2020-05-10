package dal.dao.impl;

import bll.exeptions.NoSuchUserException;
import dal.control.conection.ConnectionAdapeter;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.UserDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.RoleType;
import dal.entity.User;
import dal.exeptions.DBRuntimeException;
import dal.exeptions.OccupiedLoginException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


public class JDBCUserDao extends JDBCAbstractGenericDao<User> implements UserDao {
    private static Logger log = LogManager.getLogger(JDBCUserDao.class);

    private static final String USER_FIND_BY_EMAIL = "user.find.by.email";
    private static final String USER_REPLENISH_BALANCE = "user.replenish.balance";

    private static final String USER_SAVE = "user.save";
    private static final String GET_USER_BALANCE_IF_ENOGFE_MONEY =
            "user.get.user.bulance.if.enought.money";


    public JDBCUserDao(ResourceBundle resourceBundleRequests, TransactionalManager connector) {
        super(resourceBundleRequests, connector);
        log.debug("created");

    }

    public Optional<User> findByEmailAndPasswordWithPermissions(String email, String password) {
        log.debug("findByEmailAndPasswordWithPermissions");

        ResultSetToEntityMapper<User> mapper = getUserResultSetToEntityMapper();

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_FIND_BY_EMAIL))) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            Optional<User> user;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                user = resultSet.next() ? mapper.map(resultSet) : Optional.empty();
            }
            return user;
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }
    }

    private ResultSetToEntityMapper<User> getUserResultSetToEntityMapper() {
        return resultSet -> Optional.of(User.builder()
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
    }

    @Override
    public void replenishUserBalance(long userId, long money) throws NoSuchUserException {
        log.debug("replenishUserBalance");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_REPLENISH_BALANCE))) {
            preparedStatement.setLong(1, money);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new NoSuchUserException();
        }
    }


    public void save(String email, String password) throws OccupiedLoginException {
        log.debug("save");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_SAVE))) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new OccupiedLoginException();
        }
    }

    public boolean replenishUserBalenceOnSumeIfItPosible(long userId, long sumWhichUserNeed) throws SQLException {
        log.debug("replenishUserBalenceOnSumeIfItPosible");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_USER_BALANCE_IF_ENOGFE_MONEY))) {
            preparedStatement.setLong(1, sumWhichUserNeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, sumWhichUserNeed);
            return preparedStatement.executeUpdate() > 0;
        }
    }
}
