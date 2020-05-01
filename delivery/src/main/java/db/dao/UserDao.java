package db.dao;

import entity.User;
import exeptions.NoSuchUserException;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {

    Optional<User> findByEmailAndPasswordWithPermissions(String email, String password);

    void replenishUserBalance(long userId, long amountMoney) throws NoSuchUserException;

    public boolean replenishUserBalenceOnSumeIfItPosible(long userId, long sumWhichUserNeed);
}
