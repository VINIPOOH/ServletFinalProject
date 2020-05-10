package dal.dao.impl;

import bll.exeptions.AskedDataIsNotExist;
import dal.control.conection.ConnectionAdapeter;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.BillDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.*;
import dal.exeptions.DBRuntimeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.impl.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class JDBCBillDao extends JDBCAbstractGenericDao<Bill> implements BillDao {
    private static Logger log = LogManager.getLogger(JDBCBillDao.class);

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
        log.debug("created");
    }


    @Override
    public List<Bill> getInfoToPayBillByUserId(long userId, Locale locale) {
        log.debug("getInfoToPayBillByUserId");

        ResultSetToEntityMapper<Bill> mapper = getBillResultSetToEntityMapper(locale);

        if (locale.getLanguage().equals("ru")) {
            return findAllByLongParam(userId, resourceBundleRequests.getString(BILL_INFO_TO_PAY_BILL_BY_USER_ID_RU), mapper);
        } else {
            return findAllByLongParam(userId, resourceBundleRequests.getString(BILL_INFO_TO_PAY_BILL_BY_USER_ID_EN), mapper);
        }
    }

    private ResultSetToEntityMapper<Bill> getBillResultSetToEntityMapper(Locale locale) {
        return resultSet -> {
            Bill toReturn = Bill.builder()
                    .id(resultSet.getLong("bill_id"))
                    .costInCents(resultSet.getLong("price"))
                    .delivery(Delivery.builder()
                            .addressee(User.builder().email(resultSet.getString("addressee_email")).build())
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
        };
    }

    @Override
    public long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist {
        log.debug("getBillCostIfItIsNotPaid");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_BILL_PRISE_IF_NOT_PAID))) {
            preparedStatement.setLong(1, billId);
            preparedStatement.setLong(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    long d = resultSet.getLong(1);
                    return d;
                }
            }
            throw new AskedDataIsNotExist();
        } catch (SQLException e) {

            System.out.println(e);
            throw new DBRuntimeException();
        }
    }


    @Override
    public List<Bill> getHistoricBailsByUserId(long userId) {
        log.debug("getHistoricBailsByUserId");

        ResultSetToEntityMapper<Bill> mapper = getBillResultSetToEntityMapper();
        return findAllByLongParam(userId, resourceBundleRequests.getString(BILLS_HISTORY_BY_USER_ID), mapper);
    }

    private ResultSetToEntityMapper<Bill> getBillResultSetToEntityMapper() {
        return resultSet -> Optional.of(Bill.builder()
                .id(resultSet.getLong("id"))
                .delivery(Delivery.builder().id(resultSet.getLong("delivery_id")).build())
                .isDeliveryPaid(resultSet.getBoolean("is_delivery_paid"))
                .costInCents(resultSet.getLong("cost_in_cents"))
                .dateOfPay(resultSet.getTimestamp("date_of_pay").toLocalDateTime().toLocalDate())
                .build());
    }


    private long prepareAndExecuteQuery(long userId, long billId, PreparedStatement preparedStatement) throws SQLException, AskedDataIsNotExist {
        preparedStatement.setLong(1, billId);
        preparedStatement.setLong(2, userId);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        throw new AskedDataIsNotExist();
    }

    public boolean murkBillAsPayed(long billId) throws SQLException {
        log.debug("murkBillAsPayed");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(SET_BILL_IS_PAID_TRUE))) {
            preparedStatement.setLong(1, billId);
            return preparedStatement.executeUpdate() > 0;
        }
    }


    public boolean createBill(long deliveryId, long userId, long localitySandID, long localityGetID, int weight) throws SQLException {
        log.debug("createBill");

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
