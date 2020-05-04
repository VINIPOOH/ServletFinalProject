package dal.dao.impl;


import dal.dao.conection.ConnectionWithRestrictedAbilities;
import dal.dao.conection.DbConnectionPoolHolder;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.exeptions.DBRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class JDBCAbstractGenericDao<E> {

    protected final ResourceBundle resourceBundleRequests;
    protected final DbConnectionPoolHolder connector;

    public JDBCAbstractGenericDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        this.resourceBundleRequests = resourceBundleRequests;
        this.connector = connector;
    }

    public Optional<E> findByLongParam(Long param, String query, ResultSetToEntityMapper<E> mapper) {
        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, param);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? mapper.map(resultSet) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }
    }

    public List<E> findAllByLongParam(long param, String query, ResultSetToEntityMapper<E> mapper) {
        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, param);
            List<E> result;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    mapper.map(resultSet).ifPresent(result::add);
                }
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e);
            throw new DBRuntimeException();
        }
    }




    public List<E> findAll(String query, ResultSetToEntityMapper<E> mapper) {
        try (ConnectionWithRestrictedAbilities connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            List<E> result;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    mapper.map(resultSet).ifPresent(result::add);
                }
            }
            return result;
        } catch (SQLException e) {
            throw new DBRuntimeException();
        }
    }



}
