package dal.dao.maper;

import entity.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserEntityToPreparedStatmentMapper implements EntityToPreparedStatmentMapper<User> {
    @Override
    public void insertStatementMapper(User entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getEmail());
        preparedStatement.setString(2, entity.getPassword());
    }

    @Override
    public void updateStatementMapper(User entity, PreparedStatement preparedStatement) throws SQLException {

    }

}
