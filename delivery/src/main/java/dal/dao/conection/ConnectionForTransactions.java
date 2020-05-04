package dal.dao.conection;

import java.sql.SQLException;

public interface ConnectionForTransactions extends ConnectionWithRestrictedAbilities {
    void rollBack() throws SQLException;
    void commit() throws SQLException;
    void setReadyToClose(boolean readyToClose);
}
