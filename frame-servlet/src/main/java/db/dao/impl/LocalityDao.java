package db.dao.impl;

import db.conection.DbConnectionPoolHolder;
import dto.LocaliseWithoutConectionLocalityDto;
import entity.Locality;

import java.util.ResourceBundle;

public class LocalityDao extends JDBCAbstractGenericDao {
    private final String SELECT_BY_ID="locality.select.by.id";
    private final String FIND_ALL="locality.find.all";

    public LocalityDao(ResourceBundle resourceBundleRequests, DbConnectionPoolHolder connector) {
        super(resourceBundleRequests, connector);
    }

    public LocaliseWithoutConectionLocalityDto findAllLocaliseWithoutConnection(){

        return findAll(resourceBundleRequests.getString(FIND_ALL),)
    }

}
