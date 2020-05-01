package dal.dao.impl;

import dal.conection.DbConnectionPoolHolder;
import dal.dao.DeliveryDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dto.DeliveryInfoToGetDto;
import entity.Delivery;
import exeptions.AskedDataIsNotExist;
import exeptions.DBRuntimeException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class JDBCDeliveryDao extends JDBCAbstractGenericDao<Delivery> implements DeliveryDao {

    private String CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID =
            "create.delivery.by.weight.id.locality.send.idlocality.get.adresee.email.adresser.id";
    private String DELIVERY_INFO_TO_GET_BY_USER_ID =
            "delivery.get.not.recived.deliveries.by.user.id";
    private String SET_DELIVERY_RECIWED_STATUSE_TRUE =
            "delivery.set.recived.statuse.true";

    public JDBCDeliveryDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
    }

    @Override
    public long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     resourceBundleRequests.getString(CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID), Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1,addreeseeEmail);
            preparedStatement.setLong(2,addresserId);
            preparedStatement.setLong(3, localitySandID);
            preparedStatement.setLong(4, localityGetID);
            preparedStatement.setInt(5, weight);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                return resultSet.getLong(1);
            }throw new AskedDataIsNotExist("ddsd");
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    @Override
    public List<DeliveryInfoToGetDto> getDeliveryInfoToGet(long userId) {
        ResultSetToEntityMapper<DeliveryInfoToGetDto> mapper = (resultSet -> {
            return Optional.of(DeliveryInfoToGetDto.builder()
            .addresserEmail(resultSet.getString("email"))
            .deliveryId(resultSet.getLong("id"))
            .localitySandName(resultSet.getString("locality_sand_name"))
            .localityGetName(resultSet.getString("locality_get_name"))
            .build());
        });
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(DELIVERY_INFO_TO_GET_BY_USER_ID))) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<DeliveryInfoToGetDto> result = new ArrayList<>();
            while (resultSet.next()) {
                mapper.map(resultSet).ifPresent(result::add);
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    public void confirmGettingDelivery(long userId,long deliveryId){

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(SET_DELIVERY_RECIWED_STATUSE_TRUE))) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2,deliveryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }

    }


}
