package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.dao.WayDao;
import db.dao.maper.ResultSetToEntityMapper;
import dto.DeliveryCostAndTimeDto;
import entity.Way;
import exeptions.AskedDataIsNotExist;
import exeptions.DBRuntimeException;

import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class JDBCWayDao extends JDBCAbstractGenericDao<Way> implements WayDao {
    private String GET_COST_AND_TIME_ON_DELIVERY_BY_LOCALITY_SEND_ID_LOCALITY_GET_ID_DELIVERY_WEIGHT =
            "way.find.price.and.time.by.locality_send_id.and.locality_get_id.and.weight";
    private String GET_COST_ON_DELIVERY_BY_LOCALITY_SEND_ID_LOCALITY_GET_ID_DELIVERY_WEIGHT =
            "way.find.price.by.locality_send_id.and.locality_get_id.and.weight";


    public JDBCWayDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
    }

    @Override
    public Optional<DeliveryCostAndTimeDto> findByLocalitySand_IdAndLocalityGet_Id(long localitySandID, long localityGetID, int weight) {
        ResultSetToEntityMapper<DeliveryCostAndTimeDto> mapper = getDeliveryCostAndTimeDtoResultSetToEntityMapper();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.
                     getString(GET_COST_AND_TIME_ON_DELIVERY_BY_LOCALITY_SEND_ID_LOCALITY_GET_ID_DELIVERY_WEIGHT))) {

            preparedStatement.setLong(1, localitySandID);
            preparedStatement.setLong(2, localityGetID);
            preparedStatement.setInt(3, weight);
            preparedStatement.setInt(4, weight);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? mapper.map(resultSet) : Optional.empty();
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }

    }

    private ResultSetToEntityMapper<DeliveryCostAndTimeDto> getDeliveryCostAndTimeDtoResultSetToEntityMapper() {
        return resultSet -> Optional.of(DeliveryCostAndTimeDto.builder()
                .costInCents(resultSet.getLong("price"))
                .timeOnWayInHours(resultSet.getInt("time_on_way_in_days"))
                .build());
    }

    public long getPrise(long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist {

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.
                     getString(GET_COST_ON_DELIVERY_BY_LOCALITY_SEND_ID_LOCALITY_GET_ID_DELIVERY_WEIGHT))) {

            preparedStatement.setLong(1, localitySandID);
            preparedStatement.setLong(2, localityGetID);
            preparedStatement.setInt(3, weight);
            preparedStatement.setInt(4, weight);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("price");
            } throw new AskedDataIsNotExist("dd");
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }
    }



}
