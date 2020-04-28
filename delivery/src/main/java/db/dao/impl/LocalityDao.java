package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import db.dao.maper.ResultSetToEntityMapper;
import dto.LocaliseLocalityDto;
import exeptions.DBRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LocalityDao extends JDBCAbstractGenericDao {
    private final String SELECT_BY_ID = "locality.select.by.id";
    private final String FIND_ALL = "locality.find.all";

    public LocalityDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
    }


    public List<LocaliseLocalityDto> findAllLocaliseLocalitiesWithoutConnection() {

        ResultSetToEntityMapper<LocaliseLocalityDto> mapper = GetLocaliseLocalityMaper();

        try (Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(resourceBundleRequests.getString(FIND_ALL))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<LocaliseLocalityDto> result = new ArrayList<>();
            while (resultSet.next()) {
                mapper.map(resultSet).ifPresent(result::add);
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }

    private ResultSetToEntityMapper<LocaliseLocalityDto> GetLocaliseLocalityMaper() {
        return resultSet -> Optional.of(LocaliseLocalityDto.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .build());
    }

}
