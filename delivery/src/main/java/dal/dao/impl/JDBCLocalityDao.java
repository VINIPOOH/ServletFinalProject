package dal.dao.impl;

import dal.conection.ConnectionAdapeter;
import dal.conection.pool.ConnectionManager;
import dal.dao.LocalityDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.Locality;
import dal.exeption.DBRuntimeException;
import infrastructure.anotation.HasParentWhichNeedConfig;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static dal.dao.DBConstants.RUSSIAN_LANG_COD;

@Singleton
@HasParentWhichNeedConfig
public class JDBCLocalityDao extends JDBCAbstractGenericDao<Locality> implements LocalityDao {
    private static final String LOCALITY_FIND_ALL_RU = "locality.find.all.ru";
    private static final String LOCALITY_FIND_ALL_EN = "locality.find.all.en";
    private static final String ID = "id";
    private static final String LOCALITY_NAME = "name";
    private static Logger log = LogManager.getLogger(JDBCLocalityDao.class);

    public JDBCLocalityDao(ResourceBundle resourceBundleRequests, ConnectionManager connector) {
        super(resourceBundleRequests, connector);
        log.debug("created");
    }

    public JDBCLocalityDao() {
    }

    @Override
    public List<Locality> findAllLocaliseLocalitiesWithoutConnection(Locale locale) {
        log.debug("findAllLocaliseLocalitiesWithoutConnection");

        ResultSetToEntityMapper<Locality> mapper = getLocaliseLocalityMapper(locale);
        String localedQuery;
        localedQuery = locale.getLanguage().equals(RUSSIAN_LANG_COD) ?
                resourceBundleRequests.getString(LOCALITY_FIND_ALL_RU) :
                resourceBundleRequests.getString(LOCALITY_FIND_ALL_EN);
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(localedQuery)) {
            List<Locality> result;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
            }
            return result;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    private ResultSetToEntityMapper<Locality> getLocaliseLocalityMapper(Locale locale) {
        return resultSet -> Locality.builder()
                .id(resultSet.getLong(ID))
                .nameRu(locale.getLanguage().equals(RUSSIAN_LANG_COD) ? resultSet.getString(LOCALITY_NAME) : null)
                .nameEn(!locale.getLanguage().equals(RUSSIAN_LANG_COD) ? resultSet.getString(LOCALITY_NAME) : null)
                .build();
    }

}
