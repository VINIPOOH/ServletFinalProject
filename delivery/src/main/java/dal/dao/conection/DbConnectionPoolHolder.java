package dal.dao.conection;

import java.sql.Connection;

public interface DbConnectionPoolHolder {
    Connection getConnection();
}
