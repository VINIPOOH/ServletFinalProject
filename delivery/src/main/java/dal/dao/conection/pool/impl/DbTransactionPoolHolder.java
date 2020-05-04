package dal.dao.conection.pool.impl;

import dal.dao.conection.ConnectionWithRestrictedAbilities;
import dal.dao.conection.impl.ConnectionTransactionalProxy;
import dal.dao.conection.pool.DbConnectionPoolHolder;

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
