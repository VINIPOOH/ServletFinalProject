package dal.dao.conection;

import java.sql.Connection;

public interface DbConnectionPoolHolder {
    ConnectionWithRestrictedAbilities getConnection();
    Connection getPureConnection();
}
