package dal.dao.impl;

import dal.dao.DeliveryDao;
import dal.dao.conection.ConnectionWithRestrictedAbilities;
import dal.dao.conection.pool.DbConnectionPoolHolder;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.Delivery;
import dal.entity.Locality;
import dal.entity.User;
import dal.entity.Way;
import dal.exeptions.DBRuntimeException;
import exeptions.AskedDataIsNotExist;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class JDBCDeliveryDao extends JDBCAbstractGenericDao<Delivery> implements DeliveryDao {

    private String DELIVERY_INFO_TO_GET_BY_USER_ID =
            "delivery.get.not.recived.deliveries.by.user.id";
    private String SET_DELIVERY_RECIWED_STATUSE_TRUE =
            "delivery.set.recived.statuse.true";
    private String CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID =
            "create.delivery.by.weight.id.locality.send.idlocality.get.adresee.email.adresser.id";

    public JDBCDeliveryDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
    }


    @Override
    public List<Delivery> getDeliveryInfoToGet(long userId) {
        ResultSetToEntityMapper<Delivery> mapper = (resultSet -> Optional.of(Delivery.builder()
                .id(resultSet.getLong("id"))
                .addresser(User.builder().email(resultSet.getString("email")).build())
                .way(Way.builder()
                        .localitySand(Locality.builder().nameEn(resultSet.getString("locality_sand_name")).build())
                        .localityGet(Locality.builder().nameEn(resultSet.getString("locality_get_name")).build())
                        .build())
                .build()));
        return findAllByLongParam(userId, resourceBundleRequests.getString(DELIVERY_INFO_TO_GET_BY_USER_ID), mapper);
    }

    public void confirmGettingDelivery(long userId, long deliveryId) {

        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(SET_DELIVERY_RECIWED_STATUSE_TRUE))) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, deliveryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }

    }

    public long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist {
        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
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
            System.out.println(e);
            throw new RuntimeException();
        }
    }


}
