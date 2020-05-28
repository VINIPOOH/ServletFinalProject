package dal.dao.impl;


import dal.dao.AbstractGenericDao;
import dal.dao.maper.EntityToPreparedStatmentMapper;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.exeption.DBRuntimeException;
import dal.persistance.conection.ConnectionAdapeter;
import dal.persistance.conection.pool.ConnectionManager;
import infrastructure.anotation.InjectByType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

abstract class JDBCAbstractGenericDao<E> implements AbstractGenericDao<E> {

    private static Logger log = LogManager.getLogger(JDBCAbstractGenericDao.class);
    @InjectByType
    protected ResourceBundle resourceBundleRequests;
    @InjectByType
    protected ConnectionManager connector;

    public JDBCAbstractGenericDao() {
    }

    public JDBCAbstractGenericDao(ResourceBundle resourceBundleRequests, ConnectionManager connector) {
        this.resourceBundleRequests = resourceBundleRequests;
        this.connector = connector;
    }

    @Override
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

    @Override
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

    @Override
    public long countAllByLongParam(long param, String query) {
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

    @Override
    public boolean save(E entity, String saveQuery, EntityToPreparedStatmentMapper<E> mapper) {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {

            mapper.map(entity, preparedStatement);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public Optional<E> findById(long id, String query, ResultSetToEntityMapper<E> mapper) {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? Optional.of(mapper.map(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public boolean deleteById(long id, String query, ResultSetToEntityMapper<E> mapper) {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public List<E> findAll(String query, ResultSetToEntityMapper<E> mapper) {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<E> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(mapper.map(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public boolean update(E entity, String saveQuery, EntityToPreparedStatmentMapper<E> mapper) {
        try (ConnectionAdapeter connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {

            mapper.map(entity, preparedStatement);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

}
