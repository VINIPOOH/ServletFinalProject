package dal.dao.impl;

import dal.control.conection.ConnectionAdapeter;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.LocalityDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.Locality;
import dal.exeptions.DBRuntimeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JDBCLocalityDao extends JDBCAbstractGenericDao<Locality> implements LocalityDao {
    private static Logger log = LogManager.getLogger(JDBCLocalityDao.class);

    public JDBCLocalityDao(ResourceBundle resourceBundleRequests, TransactionalManager connector) {
        super(resourceBundleRequests, connector);
        log.debug("created");
    }


    @Override
    public List<Locality> findAllLocaliseLocalitiesWithoutConnection(Locale locale) {
        log.debug("findAllLocaliseLocalitiesWithoutConnection");

        ResultSetToEntityMapper<Locality> mapper = getLocaliseLocalityMapper(locale);
        String localedQuery;
        if (locale.getLanguage().equals("ru")) {
            localedQuery = resourceBundleRequests.getString("locality.find.all.ru");
        } else {
            localedQuery = resourceBundleRequests.getString("locality.find.all.en");
        }
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(localedQuery)) {
            List<Locality> result;
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

    private ResultSetToEntityMapper<Locality> getLocaliseLocalityMapper(Locale locale) {
        return resultSet -> {
            Locality toReturn =Locality.builder()
                    .id(resultSet.getLong("id"))
                    .build();
            if (locale.getLanguage().equals("ru")) {
                toReturn.setNameRu(resultSet.getString("name"));
            } else {
                toReturn.setNameEn(resultSet.getString("name"));
            }
            return Optional.of(toReturn);
        };
    }

}
