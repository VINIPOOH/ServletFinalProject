package dal.dao.conection.impl;

import dal.dao.conection.DbConnectionPoolHolder;
import dal.dao.conection.TransactionConnectionHandler;
import dal.exeptions.NoConnectionToDbOrConnectionIsAlreadyClosedException;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionalConnectionHolder implements TransactionConnectionHandler {
    private final Connection connection;

    public TransactionalConnectionHolder(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    protected void finalize() throws Throwable {
        if (!connection.isClosed()){
            connection.rollback();
            connection.close();
        }
    }

    @Override
    public void peeperToTransaction() throws NoConnectionToDbOrConnectionIsAlreadyClosedException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new NoConnectionToDbOrConnectionIsAlreadyClosedException();
        }
    }

    @Override
    public void rollBack() throws NoConnectionToDbOrConnectionIsAlreadyClosedException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new NoConnectionToDbOrConnectionIsAlreadyClosedException();
        }
    }

    @Override
    public void commitTransaction() throws NoConnectionToDbOrConnectionIsAlreadyClosedException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new NoConnectionToDbOrConnectionIsAlreadyClosedException();
        }
    }
}
