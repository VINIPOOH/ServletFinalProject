package dal.dao.impl;

import dal.control.conection.ConnectionAdapeter;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.DeliveryDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.*;
import dal.exeptions.AskedDataIsNotCorrect;
import dal.exeptions.DBRuntimeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static dal.dao.DBConstants.RUSSIAN_LANG_COD;

public class JDBCDeliveryDao extends JDBCAbstractGenericDao<Delivery> implements DeliveryDao {
    private static final String DELIVERY_INFO_TO_GET_BY_USER_ID_EN =
            "delivery.get.not.recived.deliveries.by.user.id.en";
    private static final String DELIVERY_INFO_TO_GET_BY_USER_ID_RU =
            "delivery.get.not.recived.deliveries.by.user.id.ru";
    private static final String SET_DELIVERY_RECIWED_STATUSE_TRUE =
            "delivery.set.recived.statuse.true";
    private static final String CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID =
            "create.delivery.by.weight.id.locality.send.idlocality.get.adresee.email.adresser.id";
    private static final String LOCALITY_SEND_COLUMN_NAME = "locality_sand_name";
    private static final String LOCALITY_GET_COLUMN_NAME = "locality_get_name";
    private static Logger log = LogManager.getLogger(JDBCDeliveryDao.class);

    public JDBCDeliveryDao(ResourceBundle resourceBundleRequests, TransactionalManager connector) {
        super(resourceBundleRequests, connector);
        log.debug("created");
    }


    @Override
    public List<Delivery> getDeliveryInfoToGet(long userId, Locale locale) {
        log.debug("getDeliveryInfoToGet");

        ResultSetToEntityMapper<Delivery> mapper = getDeliveryResultSetToEntityMapper(locale);
        if (locale.getLanguage().equals(RUSSIAN_LANG_COD)) {
            return findAllByLongParam(userId, resourceBundleRequests.getString(DELIVERY_INFO_TO_GET_BY_USER_ID_RU), mapper);
        } else {
            return findAllByLongParam(userId, resourceBundleRequests.getString(DELIVERY_INFO_TO_GET_BY_USER_ID_EN), mapper);
        }
    }

    private ResultSetToEntityMapper<Delivery> getDeliveryResultSetToEntityMapper(Locale locale) {
        return resultSet -> {
            Delivery toReturn = Delivery.builder()
                    .id(resultSet.getLong("id"))
                    .bill(Bill.builder().user(User.builder().email(resultSet.getString("email")).build()).build())
                    .way(new Way())
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
            return toReturn;
        };
    }

    public void confirmGettingDelivery(long userId, long deliveryId) {
        log.debug("confirmGettingDelivery");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(SET_DELIVERY_RECIWED_STATUSE_TRUE))) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, deliveryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }

    }

    public long createDelivery(String addreeseeEmail, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotCorrect {
        log.debug("createDelivery");

        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     resourceBundleRequests.getString(CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID), Statement.RETURN_GENERATED_KEYS)) {

            prepareAndExecuteStatment(addreeseeEmail, localitySandID, localityGetID, weight, preparedStatement);
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new AskedDataIsNotCorrect();
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new AskedDataIsNotCorrect();
        }
    }

    private void prepareAndExecuteStatment(String addreeseeEmail, long localitySandID, long localityGetID, int weight, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, addreeseeEmail);
        preparedStatement.setLong(2, localitySandID);
        preparedStatement.setLong(3, localityGetID);
        preparedStatement.setInt(4, weight);
        preparedStatement.executeUpdate();
    }


}
