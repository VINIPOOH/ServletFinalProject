package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.dao.DeliveryDao;
import entity.Delivery;
import exeptions.AskedDataIsNotExist;

import java.sql.*;
import java.util.ResourceBundle;

public class JDBCDeliveryDao extends JDBCAbstractGenericDao<Delivery> implements DeliveryDao {

    private String CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID =
            "create.delivery.by.weight.id.locality.send.idlocality.get.adresee.email.adresser.id";

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
}
