package dal.handling.transaction;

import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.handling.conection.pool.DbConnectionPoolHolder;

import java.sql.SQLException;

public interface TransactionManager extends DbConnectionPoolHolder, AutoCloseable {
    void rollBack() throws SQLException;
    void commit() throws SQLException;
    @Override
    void close() throws SQLException;
}
