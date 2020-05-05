package dal.dao;

import dal.entity.User;
import dal.exeptions.OccupiedLoginException;
import exeptions.NoSuchUserException;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {

    Optional<User> findByEmailAndPasswordWithPermissions(String email, String password);

    void save(String email, String password) throws OccupiedLoginException;

    void replenishUserBalance(long userId, long amountMoney) throws NoSuchUserException;

    boolean replenishUserBalenceOnSumeIfItPosible(long userId, long sumWhichUserNeed) throws SQLException;

}
