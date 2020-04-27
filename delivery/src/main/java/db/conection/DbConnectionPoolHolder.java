package db.conection;

import java.sql.Connection;

public interface DbConnectionPoolHolder {
    Connection getConnection();
}
