package dal.dao.impl;

import dal.dao.BillDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.*;
import dal.exeptions.DBRuntimeException;
import dal.handling.conection.ConnectionAdapeter;
import dal.handling.conection.pool.TransactionalManager;
import exeptions.AskedDataIsNotExist;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class JDBCBillDao extends JDBCAbstractGenericDao<Bill> implements BillDao {
    private static final String BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID =
            "bill.create.by.cost.delivery.id.user.id";
    private static final String BILL_INFO_TO_PAY_BILL_BY_USER_ID_EN =
            "bill.pay.info.sellect.by.sender.id.en";
    private static final String GET_BILL_PRISE_IF_NOT_PAID =
            "bill.get.prise.if.not.paid";
    private static final String SET_BILL_IS_PAID_TRUE =
            "bill.set.is.paid.true";
    private static final String BILLS_HISTORY_BY_USER_ID =
            "bill.history.by.user.id";
    private static final String BILL_INFO_TO_PAY_BILL_BY_USER_ID_RU =
            "bill.pay.info.sellect.by.sender.id.ru";


    public JDBCBillDao(ResourceBundle resourceBundleRequests, TransactionalManager connector) {
        super(resourceBundleRequests, connector);
    }


    @Override
    public List<Bill> getInfoToPayBillByUserId(long userId, Locale locale) {
        ResultSetToEntityMapper<Bill> mapper = (resultSet -> {
            Bill toReturn = Bill.builder()
                    .id(resultSet.getLong("bill_id"))
                    .costInCents(resultSet.getLong("price"))
                    .delivery(Delivery.builder()
                            .addresser(User.builder().email(resultSet.getString("addresser_email")).build())
                            .id(resultSet.getLong("delivery_id"))
                            .weight(resultSet.getInt("weight"))
                            .build())
                    .build();
            if (locale.getLanguage().equals("ru")) {
                toReturn.getDelivery().setWay(Way.builder()
                        .localityGet(Locality.builder().nameRu(resultSet.getString("locality_get_name")).build())
                        .localitySand(Locality.builder().nameRu(resultSet.getString("locality_sand_name")).build())
                        .build());
            } else {
                toReturn.getDelivery().setWay(Way.builder()
                        .localityGet(Locality.builder().nameEn(resultSet.getString("locality_get_name")).build())
                        .localitySand(Locality.builder().nameEn(resultSet.getString("locality_sand_name")).build())
                        .build());
            }
            return Optional.of(toReturn);
        });

        if (locale.getLanguage().equals("ru")) {
            return findAllByLongParam(userId, resourceBundleRequests.getString(BILL_INFO_TO_PAY_BILL_BY_USER_ID_RU), mapper);
        } else {
            return findAllByLongParam(userId, resourceBundleRequests.getString(BILL_INFO_TO_PAY_BILL_BY_USER_ID_EN), mapper);
        }
    }

    @Override
    public long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist {
        try (ConnectionAdapeter connection = connector.getConnection();
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


    public long getBillPrice(long userId, long billId) throws SQLException, AskedDataIsNotExist {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_BILL_PRISE_IF_NOT_PAID))) {
            {
                preparedStatement.setLong(1, billId);
                preparedStatement.setLong(2, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getLong(1);
                    }
                }
                throw new AskedDataIsNotExist();
            }
        }
    }

    public boolean murkBillAsPayed(long billId) throws SQLException {

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(SET_BILL_IS_PAID_TRUE))) {
            preparedStatement.setLong(1, billId);
            return preparedStatement.executeUpdate() > 0;
        }
    }


    public boolean createBill(long deliveryId, long userId, long localitySandID, long localityGetID, int weight) throws SQLException {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID))) {
            preparedStatement.setLong(1, localitySandID);
            preparedStatement.setLong(2, localityGetID);
            preparedStatement.setInt(3, weight);
            preparedStatement.setInt(4, weight);
            preparedStatement.setLong(5, deliveryId);
            preparedStatement.setLong(6, userId);
            return preparedStatement.executeUpdate() != 0;
        }
    }
}
