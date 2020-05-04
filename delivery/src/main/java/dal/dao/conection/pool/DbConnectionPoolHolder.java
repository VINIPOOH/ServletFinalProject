package dal.dao.conection.pool;

import dal.dao.conection.ConnectionWithRestrictedAbilities;

import java.sql.Connection;

public interface DbConnectionPoolHolder {
    ConnectionWithRestrictedAbilities getConnection();
    Connection getPureConnection();
}
