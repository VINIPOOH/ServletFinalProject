package dal.handling.conection.pool.impl;

import dal.handling.conection.ConnectionAdapeter;
import dal.handling.conection.impl.ConnectionTransactionalProxy;
import dal.handling.conection.pool.DbConnectionPoolHolder;

import java.sql.Connection;

public class DbTransactionPoolHolder implements DbConnectionPoolHolder {
    private final ConnectionTransactionalProxy connectionTransactionalProxy;

    public DbTransactionPoolHolder(ConnectionTransactionalProxy connectionTransactionalProxy) {
        this.connectionTransactionalProxy = connectionTransactionalProxy;
    }


    @Override
    public ConnectionAdapeter getConnection() {
        return connectionTransactionalProxy;
    }


}
