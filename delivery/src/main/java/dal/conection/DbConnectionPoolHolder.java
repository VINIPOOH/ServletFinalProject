package dal.conection;

import java.sql.Connection;

public interface DbConnectionPoolHolder {
    Connection getConnection();
}
