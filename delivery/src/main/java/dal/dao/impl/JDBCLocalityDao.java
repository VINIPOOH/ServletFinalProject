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

import static dal.dao.DBConstants.RUSSIAN_LANG_COD;

public class JDBCLocalityDao extends JDBCAbstractGenericDao<Locality> implements LocalityDao {
    public static final String LOCALITY_FIND_ALL_RU = "locality.find.all.ru";
    public static final String LOCALITY_FIND_ALL_EN = "locality.find.all.en";
    public static final String ID = "id";
    public static final String LOCALITY_NAME = "name";
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
        if (locale.getLanguage().equals(RUSSIAN_LANG_COD)) {
            localedQuery = resourceBundleRequests.getString(LOCALITY_FIND_ALL_RU);
        } else {
            localedQuery = resourceBundleRequests.getString(LOCALITY_FIND_ALL_EN);
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
                    .id(resultSet.getLong(ID))
                    .build();
            if (locale.getLanguage().equals(RUSSIAN_LANG_COD)) {
                toReturn.setNameRu(resultSet.getString(LOCALITY_NAME));
            } else {
                toReturn.setNameEn(resultSet.getString(LOCALITY_NAME));
            }
            return Optional.of(toReturn);
        };
    }

}
