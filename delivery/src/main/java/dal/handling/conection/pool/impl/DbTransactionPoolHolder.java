package dal.handling.conection.pool.impl;

import dal.handling.conection.ConnectionWithRestrictedAbilities;
import dal.handling.conection.impl.ConnectionTransactionalProxy;
import dal.handling.conection.pool.DbConnectionPoolHolder;

import java.sql.Connection;

public class DbTransactionPoolHolder implements DbConnectionPoolHolder {
    private final ConnectionTransactionalProxy connectionTransactionalProxy;

    public DbTransactionPoolHolder(ConnectionTransactionalProxy connectionTransactionalProxy) {
        this.connectionTransactionalProxy = connectionTransactionalProxy;
    }


    @Override
    public ConnectionWithRestrictedAbilities getConnection() {
        return connectionTransactionalProxy;
    }

    @Override
    public Connection getPureConnection() {
        return connectionTransactionalProxy.getSubject();
    }
}
