package dal.handling.conection.pool;

import dal.handling.conection.ConnectionAdapeter;

import java.sql.Connection;

public interface DbConnectionPoolHolder {
    ConnectionAdapeter getConnection();
    Connection getPureConnection();
}
