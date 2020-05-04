package dal.handling.conection.pool;

import dal.handling.conection.ConnectionWithRestrictedAbilities;

import java.sql.Connection;

public interface DbConnectionPoolHolder {
    ConnectionWithRestrictedAbilities getConnection();
    Connection getPureConnection();
}
