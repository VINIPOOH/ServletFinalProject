package dal.handling.transaction;

import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;

import java.sql.SQLException;

public interface TransactionManager extends  AutoCloseable {
    void rollBack() throws SQLException;
    void commit() throws SQLException;
    BillDao getBillDao();
    UserDao getUserDao();
    DeliveryDao getDeliveryDao();
    @Override
    void close() throws SQLException;
}
