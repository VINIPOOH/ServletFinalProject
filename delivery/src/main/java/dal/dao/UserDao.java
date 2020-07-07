package dal.dao;

import dal.entity.User;
import dal.exeption.AskedDataIsNotCorrect;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Declares an interface for work with {@link User}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface UserDao {

    Optional<User> findByEmailAndPasswordWithPermissions(String email, String password);

    boolean save(String email, String password) throws AskedDataIsNotCorrect;

    boolean replenishUserBalance(long userId, long amountMoney) throws AskedDataIsNotCorrect;

    boolean replenishUserBalenceOnSumeIfItPosible(long userId, long sumWhichUserNeed) throws SQLException;

    long getUserBalanceByUserID(long userId) throws AskedDataIsNotCorrect;

    List<User> getAllUsers();
}
