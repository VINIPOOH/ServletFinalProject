package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.dao.BillDao;
import db.dao.maper.ResultSetToEntityMapper;
import dto.BillDto;
import dto.BillInfoToPayDto;
import entity.Bill;
import entity.User;
import exeptions.AskedDataIsNotExist;
import exeptions.DBRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.time.LocalDate;

import static db.dao.UserDaoConstants.USER_FIND_BY_EMAIL;

public class JDBCBillDao extends JDBCAbstractGenericDao<Bill> implements BillDao {
    private final String BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID=
            "bill.create.by.cost.delivery.id.user.id";
    private final String BILL_INFO_TO_PAY_BILL_BY_USER_ID=
            "bill.pay.info.sellect.by.sender.id";
    private final String GET_BILL_PRISE_IF_NOT_PAID=
            "bill.get.prise.if.not.paid";
    private final String SET_BILL_IS_PAID_TRUE =
            "bill.set.is.paid.true";
    private final String BILLS_HISTORY_BY_USER_ID =
            "bill.history.by.user.id";

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

    @Override
    public List<BillInfoToPayDto> getInfoToPayBillByUserId(long user_id) {
        ResultSetToEntityMapper<BillInfoToPayDto> mapper = (resultSet -> {
            return Optional.of(BillInfoToPayDto.builder()
                    .addreeserEmail(resultSet.getString("addresser_email"))
                    .bill_id(resultSet.getLong("bill_id"))
                    .delivery_id(resultSet.getLong("delivery_id"))
                    .localityGetName(resultSet.getString("locality_get_name"))
                    .localitySandName(resultSet.getString("locality_sand_name"))
                    .price(resultSet.getLong("price"))
                    .weight(resultSet.getInt("weight"))
                    .build());
        });
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(BILL_INFO_TO_PAY_BILL_BY_USER_ID))) {
            preparedStatement.setLong(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BillInfoToPayDto> result = new ArrayList<>();
            while (resultSet.next()) {
                mapper.map(resultSet).ifPresent(result::add);
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(GET_BILL_PRISE_IF_NOT_PAID))) {
            preparedStatement.setLong(1,billId);
            preparedStatement.setLong(2,userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getLong(1);
            }throw new AskedDataIsNotExist("sd");
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
            return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public List<BillDto> getHistoricBailsByUserId(long userId) {
        ResultSetToEntityMapper<BillDto> mapper = (resultSet -> {
            return Optional.of(BillDto.builder()
                    .id(resultSet.getLong("id"))
                    .deliveryId(resultSet.getLong("delivery_id"))
                    .isDeliveryPaid(resultSet.getBoolean("is_delivery_paid"))
                    .costInCents(resultSet.getLong("cost_in_cents"))
                    .dateOfPay(resultSet.getTimestamp("date_of_pay").toLocalDateTime().toLocalDate())
                    .build());
        });
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(BILLS_HISTORY_BY_USER_ID))) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BillDto> result = new ArrayList<>();
            while (resultSet.next()) {
                mapper.map(resultSet).ifPresent(result::add);
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }
}
