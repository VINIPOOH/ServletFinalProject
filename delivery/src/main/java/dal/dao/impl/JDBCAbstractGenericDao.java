package dal.dao.impl;


import dal.dao.conection.DbConnectionPoolHolder;
import dal.dao.maper.ResultSetToEntityMapper;
import exeptions.DBRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class JDBCAbstractGenericDao<E>{

    protected final ResourceBundle resourceBundleRequests;
    protected final DbConnectionPoolHolder connector;

    public JDBCAbstractGenericDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        this.resourceBundleRequests = resourceBundleRequests;
        this.connector = connector;
    }
//    public E save(E dal.entity) throws SQLException {
//        try (Connection connection = connector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
//
//            entityToPreparedStatmentMapper.insertStatementMapper(dal.entity, preparedStatement);
//            if (preparedStatement.executeUpdate() != 0) {
//                return dal.entity;
//            } else {
//                throw new SQLException();
//            }
//        }
//    }
    public Optional<E> findByLongParam(Long param, String query, ResultSetToEntityMapper<E> mapper) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, param);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? mapper.map(resultSet) : Optional.empty();
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }
    }

    public List<E> findAllByLongParam(long param, String query, ResultSetToEntityMapper<E> mapper) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, param);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<E> result = new ArrayList<>();
            while (resultSet.next()) {
                mapper.map(resultSet).ifPresent(result::add);
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }



//    public boolean deleteById(Long id) {
//        try (Connection connection = connector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
//
//            preparedStatement.setLong(1, id);
//            return preparedStatement.executeUpdate() == 1;
//        } catch (SQLException e) {
//            throw new DBRuntimeException();
//        }
//    }

    public List<E> findAll(String query, ResultSetToEntityMapper<E> mapper) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<E> result = new ArrayList<>();
            while (resultSet.next()) {
                mapper.map(resultSet).ifPresent(result::add);
            }
            return result;
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }
    }



//    @Override
//    public boolean update(E dal.entity) {
//        try (Connection connection = connector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
//
//            entityToPreparedStatmentMapper.updateStatementMapper(dal.entity, preparedStatement);
//            return preparedStatement.executeUpdate() == 1;
//        } catch (SQLException e) {
//            throw new DBRuntimeException();
//        }
//    }





}
