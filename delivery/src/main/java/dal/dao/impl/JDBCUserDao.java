package dal.dao.impl;

import dal.control.conection.ConnectionAdapeter;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.UserDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.RoleType;
import dal.entity.User;
import dal.exeptions.AskedDataIsNotCorrect;
import dal.exeptions.DBRuntimeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


public class JDBCUserDao extends JDBCAbstractGenericDao<User> implements UserDao {
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ACCOUNT_NON_EXPIRED = "account_non_expired";
    private static final String ACCOUNT_NON_LOCKED = "account_non_locked";
    private static final String CREDENTIALS_NON_EXPIRED = "credentials_non_expired";
    private static final String ENABLED = "enabled";
    private static final String USER_MONEY_IN_CENTS = "user_money_in_cents";
    private static final String ROLE = "role";
    private static final String USER_FIND_BY_EMAIL = "user.find.by.email";
    private static final String USER_REPLENISH_BALANCE = "user.replenish.balance";
    private static final String USER_SAVE = "user.save";
    private static final String GET_USER_BALANCE_IF_ENOGFE_MONEY =
            "user.get.user.bulance.if.enought.money";
    private static final String GET_USER_BALANCE_BY_ID = "user.get.balance.by.id";
    private static Logger log = LogManager.getLogger(JDBCUserDao.class);


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
                user = resultSet.next() ? Optional.of(mapper.map(resultSet)) : Optional.empty();
            }
            return user;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    private ResultSetToEntityMapper<User> getUserResultSetToEntityMapper() {
        return resultSet -> User.builder()
                .id(resultSet.getLong(ID))
                .email(resultSet.getString(EMAIL))
                .password(resultSet.getString(PASSWORD))
                .accountNonExpired(resultSet.getBoolean(ACCOUNT_NON_EXPIRED))
                .accountNonLocked(resultSet.getBoolean(ACCOUNT_NON_LOCKED))
                .credentialsNonExpired(resultSet.getBoolean(CREDENTIALS_NON_EXPIRED))
                .enabled(resultSet.getBoolean(ENABLED))
                .userMoneyInCents(resultSet.getLong(USER_MONEY_IN_CENTS))
                .roleType(RoleType.valueOf(resultSet.getString(ROLE)))
                .build();
    }

    @Override
    public boolean replenishUserBalance(long userId, long money) throws AskedDataIsNotCorrect {
        log.debug("replenishUserBalance");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_REPLENISH_BALANCE))) {
            preparedStatement.setLong(1, money);
            preparedStatement.setLong(2, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new AskedDataIsNotCorrect();
        }
    }


    public boolean save(String email, String password) throws AskedDataIsNotCorrect {
        log.debug("save");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(USER_SAVE))) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new AskedDataIsNotCorrect();
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

    @Override
    public long getUserBalanceByUserID(long userId) throws AskedDataIsNotCorrect {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_USER_BALANCE_BY_ID))) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(USER_MONEY_IN_CENTS);
                }
                throw new AskedDataIsNotCorrect();
            }
        } catch (SQLException e) {
            log.error("SQLException", e);

            throw new AskedDataIsNotCorrect();
        }
    }
}
