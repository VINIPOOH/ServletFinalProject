package dal.dao.impl;

import dal.dao.DeliveryDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.Delivery;
import dal.entity.Locality;
import dal.entity.User;
import dal.entity.Way;
import dal.exeptions.DBRuntimeException;
import dal.handling.conection.ConnectionAdapeter;
import dal.handling.conection.pool.TransactionalManager;
import exeptions.AskedDataIsNotExist;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class JDBCDeliveryDao extends JDBCAbstractGenericDao<Delivery> implements DeliveryDao {

    private static final String DELIVERY_INFO_TO_GET_BY_USER_ID_EN =
            "delivery.get.not.recived.deliveries.by.user.id.en";
    private static final String DELIVERY_INFO_TO_GET_BY_USER_ID_RU =
            "delivery.get.not.recived.deliveries.by.user.id.ru";
    private static final String SET_DELIVERY_RECIWED_STATUSE_TRUE =
            "delivery.set.recived.statuse.true";
    private static final String CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID =
            "create.delivery.by.weight.id.locality.send.idlocality.get.adresee.email.adresser.id";

    private  static final String LOCALITY_SEND_COLUMN_NAME ="locality_sand_name";
    private static final String LOCALITY_GET_COLUMN_NAME="locality_get_name";

    public JDBCDeliveryDao(ResourceBundle resourceBundleRequests, TransactionalManager connector) {
        super(resourceBundleRequests, connector);
    }


    @Override
    public List<Delivery> getDeliveryInfoToGet(long userId, Locale locale) {
        ResultSetToEntityMapper<Delivery> mapper = (resultSet -> {
            Delivery toReturn = Delivery.builder()
                    .id(resultSet.getLong("id"))
                    .addresser(User.builder().email(resultSet.getString("email")).build())
                    .way(Way.builder()
                            .localitySand(Locality.builder().nameEn(resultSet.getString(LOCALITY_SEND_COLUMN_NAME)).build())
                            .localityGet(Locality.builder().nameEn(resultSet.getString(LOCALITY_GET_COLUMN_NAME)).build())
                            .build())
                    .build();
            if (locale.getLanguage().equals("ru")) {
                toReturn.setWay(Way.builder()
                        .localityGet(Locality.builder().nameRu(resultSet.getString(LOCALITY_GET_COLUMN_NAME)).build())
                        .localitySand(Locality.builder().nameRu(resultSet.getString(LOCALITY_SEND_COLUMN_NAME)).build())
                        .build());
            } else {
                toReturn.setWay(Way.builder()
                        .localityGet(Locality.builder().nameEn(resultSet.getString(LOCALITY_GET_COLUMN_NAME)).build())
                        .localitySand(Locality.builder().nameEn(resultSet.getString(LOCALITY_SEND_COLUMN_NAME)).build())
                        .build());
            }
            return Optional.of(toReturn);
        });
        if (locale.getLanguage().equals("ru")) {
            return findAllByLongParam(userId, resourceBundleRequests.getString(DELIVERY_INFO_TO_GET_BY_USER_ID_RU), mapper);
        } else {
            return findAllByLongParam(userId, resourceBundleRequests.getString(DELIVERY_INFO_TO_GET_BY_USER_ID_EN), mapper);
        }
    }

    public void confirmGettingDelivery(long userId, long deliveryId) {

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(SET_DELIVERY_RECIWED_STATUSE_TRUE))) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, deliveryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }

    }

    public long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
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
            throw new RuntimeException();
        }
    }


}
