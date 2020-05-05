package dal.dao;

import dal.entity.User;
import exeptions.NoSuchUserException;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {

    Optional<User> findByEmailAndPasswordWithPermissions(String email, String password);

    void save(String email, String password) throws SQLException;

    void replenishUserBalance(long userId, long amountMoney) throws NoSuchUserException;

    boolean replenishUserBalenceOnSumeIfItPosible(long userId, long sumWhichUserNeed) throws SQLException;

}
