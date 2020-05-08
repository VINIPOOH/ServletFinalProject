package dal.control.conection.impl;

import dal.control.conection.ConnectionAdapeter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionAdapterImpl implements ConnectionAdapeter {

    private final Connection connection;
    private boolean isTransaction = false;

    public ConnectionAdapterImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {

        return connection.prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public void close() throws SQLException {
        if (!isTransaction) {
            connection.close();
        }
    }

    @Override
    public void setAutoCommit(boolean isAutoCommit) throws SQLException {
        connection.setAutoCommit(isAutoCommit);
    }

    @Override
    public Connection getSubject() {
        return connection;
    }

    @Override
    public void rollBack() throws SQLException {
        connection.rollback();
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void setIsTransaction(boolean isTransaction) {
        this.isTransaction = isTransaction;
    }
}