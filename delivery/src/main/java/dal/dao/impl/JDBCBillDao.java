package dal.dao.impl;

import bll.exeptions.UnsupportableWeightFactorException;
import dal.dao.BillDao;
import dal.dao.conection.DbConnectionPoolHolder;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.*;
import dal.exeptions.DBRuntimeException;
import exeptions.AskedDataIsNotExist;
import exeptions.FailCreateDeliveryException;
import web.dto.DeliveryOrderCreateDto;

import java.sql.*;
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
    private final String GET_USER_BALANCE_IF_ENOGFE_MONEY =
            "user.get.user.bulance.if.enought.money";
    private String GET_COST_ON_DELIVERY_BY_LOCALITY_SEND_ID_LOCALITY_GET_ID_DELIVERY_WEIGHT =
            "way.find.price.by.locality_send_id.and.locality_get_id.and.weight";
    private String CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID =
            "create.delivery.by.weight.id.locality.send.idlocality.get.adresee.email.adresser.id";



    public JDBCBillDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
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

    public boolean payBill(long userId, long billId) {
        try (Connection connection = connector.getConnection()) {
            connection.setAutoCommit(false);
            long billPrise = 0;
            try {
                billPrise = getBillPrice(userId, billId, connection);
            } catch (AskedDataIsNotExist askedDataIsNotExist) {
                connection.rollback();
                return false;
            }
            boolean a = replenishUserBalenceOnSumeIfItPosible(userId, billPrise, connection);
            if (a) {
                boolean b= murkBillAsPayed(billId, connection);
                if (b) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {

            return false;
        }
    }

    private long getBillPrice(long userId, long billId, Connection connection) throws SQLException, AskedDataIsNotExist {
        try (PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_BILL_PRISE_IF_NOT_PAID))) {
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

    private boolean replenishUserBalenceOnSumeIfItPosible(long userId, long sumWhichUserNeed, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_USER_BALANCE_IF_ENOGFE_MONEY))) {
            preparedStatement.setLong(1, sumWhichUserNeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, sumWhichUserNeed);
            return preparedStatement.executeUpdate() > 0;
        }

    }

    private boolean murkBillAsPayed(long billId, Connection connection) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(SET_BILL_IS_PAID_TRUE))) {
            preparedStatement.setLong(1, billId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public boolean initializeDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) {
        try (Connection connection = connector.getConnection()) {
            connection.setAutoCommit(false);
            long price = 0;
            try {
                price = getPrise(deliveryOrderCreateDto.getLocalitySandID()
                        , deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight(), connection);
                long newDeliveryId = createDelivery(deliveryOrderCreateDto.getAddresseeEmail(), initiatorId, deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight(),connection);
                if(createBill(price, newDeliveryId, initiatorId, connection)){
                    connection.commit();
                    return true;
                }
                connection.commit();
                return false;
            } catch (AskedDataIsNotExist askedDataIsNotExist) {
                connection.rollback();
                return false;
            }
        }catch (SQLException e) {
            return false;
        }
    }

    private long getPrise(long localitySandID, long localityGetID, int weight, Connection connection) throws AskedDataIsNotExist, SQLException
        {
        try (PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.
                getString(GET_COST_ON_DELIVERY_BY_LOCALITY_SEND_ID_LOCALITY_GET_ID_DELIVERY_WEIGHT))){

               preparedStatement.setLong(1, localitySandID);
               preparedStatement.setLong(2, localityGetID);
               preparedStatement.setInt(3, weight);
               preparedStatement.setInt(4, weight);
               try (ResultSet resultSet = preparedStatement.executeQuery()) {
                   if (resultSet.next()) {
                       return resultSet.getLong("price");
                   }
               }
               throw new AskedDataIsNotExist("dd");
        }
    }
    private long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight, Connection connection) throws AskedDataIsNotExist {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                     resourceBundleRequests.getString(CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID), Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, addreeseeEmail);
            preparedStatement.setLong(2, addresserId);
            preparedStatement.setLong(3, localitySandID);
            preparedStatement.setLong(4, localityGetID);
            preparedStatement.setInt(5, weight);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new AskedDataIsNotExist("ddsd");
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    private boolean createBill(long costInCents, long deliveryId, long userId, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID))) {
            preparedStatement.setLong(1, costInCents);
            preparedStatement.setLong(2, deliveryId);
            preparedStatement.setLong(3, userId);
            return preparedStatement.executeUpdate() != 0;
        }
    }
}
