package dal.dao;

import dal.entity.User;
import dal.exeptions.AskedDataIsNotCorrect;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByEmailAndPasswordWithPermissions(String email, String password);

    boolean save(String email, String password) throws AskedDataIsNotCorrect;

    boolean replenishUserBalance(long userId, long amountMoney) throws AskedDataIsNotCorrect;

    boolean replenishUserBalenceOnSumeIfItPosible(long userId, long sumWhichUserNeed) throws SQLException;

    long getUserBalanceByUserID(long userId) throws AskedDataIsNotCorrect;
}
