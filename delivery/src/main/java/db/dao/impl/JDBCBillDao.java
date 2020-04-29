package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.dao.BillDao;
import entity.Bill;
import exeptions.DBRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JDBCBillDao extends JDBCAbstractGenericDao<Bill> implements BillDao {
    private final String BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID=
            "bill.create.by.cost.delivery.id.user.id";

    public JDBCBillDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
    }

    @Override
    public boolean createBill(long costInCents, long deliveriId, long userId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID))) {
            preparedStatement.setLong(1,costInCents);
            preparedStatement.setLong(2,deliveriId);
            preparedStatement.setLong(3, userId);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }
}
