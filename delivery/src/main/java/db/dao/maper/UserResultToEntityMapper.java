package db.dao.maper;

import entity.RoleType;
import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserResultToEntityMapper implements ResultSetToEntityMapper<User> {

    @Override
    public Optional<User> map(ResultSet resultSet) throws SQLException {
        return Optional.of(User.builder()
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
}
