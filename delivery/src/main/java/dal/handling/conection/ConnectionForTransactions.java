package dal.handling.conection;

import java.sql.SQLException;

public interface ConnectionForTransactions extends ConnectionAdapeter {
    void rollBack() throws SQLException;
    void commit() throws SQLException;
    void setReadyToClose(boolean readyToClose);
}
