package dal.dao.impl;


import dal.control.conection.ConnectionAdapeter;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.exeptions.DBRuntimeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

abstract class JDBCAbstractGenericDao<E> {

    private static Logger log = LogManager.getLogger(JDBCAbstractGenericDao.class);

    protected final ResourceBundle resourceBundleRequests;
    protected final TransactionalManager connector;

    public JDBCAbstractGenericDao(ResourceBundle resourceBundleRequests, TransactionalManager connector) {
        this.resourceBundleRequests = resourceBundleRequests;
        this.connector = connector;
    }

    public List<E> findAllByLongParam(long param, String query, ResultSetToEntityMapper<E> mapper) {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, param);
            return mapPreparedStatementToEntitiesList(mapper, preparedStatement);
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    public List<E> findAllByLongParamPageable(long param, Integer offset, Integer limit, String query, ResultSetToEntityMapper<E> mapper) {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, param);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, limit);
            return mapPreparedStatementToEntitiesList(mapper, preparedStatement);
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }

    }



    private List<E> mapPreparedStatementToEntitiesList(ResultSetToEntityMapper<E> mapper, PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            List<E> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(mapper.map(resultSet));
            }
            return result;
        }
    }

    protected long countAllByLongParam(long param, String query) {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, param);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

}
