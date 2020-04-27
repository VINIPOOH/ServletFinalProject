package db.dao;

import entity.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {

    Optional<User> findByEmailAndPasswordWithPermissions(String email, String password);
}
