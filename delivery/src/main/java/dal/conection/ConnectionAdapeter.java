package dal.conection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ConnectionAdapeter extends AutoCloseable {
    @Override
    void close() throws SQLException;

    void setAutoCommit(boolean isAutoCommit) throws SQLException;

    Connection getSubject();

    void rollBack() throws SQLException;

    void commit() throws SQLException;

    void setIsTransaction(boolean readyToClose);

    PreparedStatement prepareStatement(String sql) throws SQLException;

    PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException;
}
