package dal.dao.impl;

import bll.dto.LocaliseLocalityDto;
import dal.control.conection.ConnectionAdapeter;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.Locality;
import dal.exeptions.DBRuntimeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LocalityDao extends JDBCAbstractGenericDao<Locality> {
    private static Logger log = LogManager.getLogger(LocalityDao.class);

    public LocalityDao(ResourceBundle resourceBundleRequests, TransactionalManager connector) {
        super(resourceBundleRequests, connector);
        log.debug("created");
    }


    public List<LocaliseLocalityDto> findAllLocaliseLocalitiesWithoutConnection(Locale locale) {
        log.debug("findAllLocaliseLocalitiesWithoutConnection");

        ResultSetToEntityMapper<LocaliseLocalityDto> mapper = getLocaliseLocalityMapper();
        String localedQuery;
        if (locale.getLanguage().equals("ru")) {
            localedQuery = resourceBundleRequests.getString("locality.find.all.ru");
        } else {
            localedQuery = resourceBundleRequests.getString("locality.find.all.en");
        }
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(localedQuery)) {
            List<LocaliseLocalityDto> result;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    mapper.map(resultSet).ifPresent(result::add);
                }
            }
            return result;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    private ResultSetToEntityMapper<LocaliseLocalityDto> getLocaliseLocalityMapper() {
        return resultSet -> Optional.of(LocaliseLocalityDto.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build());
    }

}
