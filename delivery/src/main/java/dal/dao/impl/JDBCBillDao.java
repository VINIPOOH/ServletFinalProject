package dal.dao.impl;

import dal.dao.BillDao;
import dal.dao.conection.DbConnectionPoolHolder;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.*;
import dal.exeptions.DBRuntimeException;
import exeptions.AskedDataIsNotExist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class JDBCBillDao extends JDBCAbstractGenericDao<Bill> implements BillDao {
    private final String BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID =
            "bill.create.by.cost.delivery.id.user.id";
    private final String BILL_INFO_TO_PAY_BILL_BY_USER_ID =
            "bill.pay.info.sellect.by.sender.id";
    private final String GET_BILL_PRISE_IF_NOT_PAID =
            "bill.get.prise.if.not.paid";
    private final String SET_BILL_IS_PAID_TRUE =
            "bill.set.is.paid.true";
    private final String BILLS_HISTORY_BY_USER_ID =
            "bill.history.by.user.id";

    public JDBCBillDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
    }

    @Override
    public boolean createBill(long costInCents, long deliveryId, long userId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID))) {
            preparedStatement.setLong(1, costInCents);
            preparedStatement.setLong(2, deliveryId);
            preparedStatement.setLong(3, userId);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public List<Bill> getInfoToPayBillByUserId(long user_id) {
        ResultSetToEntityMapper<Bill> mapper = (resultSet -> Optional.of(Bill.builder()
                .id(resultSet.getLong("bill_id"))
                .costInCents(resultSet.getLong("price"))
                .delivery(Delivery.builder()
                        .addresser(User.builder().email(resultSet.getString("addresser_email")).build())
                        .id(resultSet.getLong("delivery_id"))
                        .weight(resultSet.getInt("weight"))
                        .way(Way.builder()
                                .localityGet(Locality.builder().nameEn(resultSet.getString("locality_get_name")).build())
                                .localitySand(Locality.builder().nameEn(resultSet.getString("locality_sand_name")).build())
                                .build())
                        .build())
                .build()));
        return findAllByLongParam(user_id, resourceBundleRequests.getString(BILL_INFO_TO_PAY_BILL_BY_USER_ID), mapper);
    }

    @Override
    public long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_BILL_PRISE_IF_NOT_PAID))) {
            preparedStatement.setLong(1, billId);
            preparedStatement.setLong(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
            throw new AskedDataIsNotExist("sd");
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public boolean murkBillAsPayed(long billId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(SET_BILL_IS_PAID_TRUE))) {
            preparedStatement.setLong(1, billId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public List<Bill> getHistoricBailsByUserId(long userId) {
        ResultSetToEntityMapper<Bill> mapper = resultSet -> Optional.of(Bill.builder()
                .id(resultSet.getLong("id"))
                .delivery(Delivery.builder().id(resultSet.getLong("delivery_id")).build())
                .isDeliveryPaid(resultSet.getBoolean("is_delivery_paid"))
                .costInCents(resultSet.getLong("cost_in_cents"))
                .dateOfPay(resultSet.getTimestamp("date_of_pay").toLocalDateTime().toLocalDate())
                .build());
        return findAllByLongParam(userId, resourceBundleRequests.getString(BILLS_HISTORY_BY_USER_ID), mapper);
    }
}
