package dal.dao.conection;

import dal.exeptions.NoConnectionToDbOrConnectionIsAlreadyClosedException;

import java.sql.SQLException;

public interface TransactionConnectionHandler extends DbConnectionPoolHolder {
    void peeperToTransaction() throws NoConnectionToDbOrConnectionIsAlreadyClosedException;
    void rollBack() throws NoConnectionToDbOrConnectionIsAlreadyClosedException;
    void commitTransaction() throws NoConnectionToDbOrConnectionIsAlreadyClosedException;
}
