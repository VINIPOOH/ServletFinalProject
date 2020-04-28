package service;


import db.dao.impl.LocalityDao;
import dto.LocaliseLocalityDto;

import java.util.List;

public class LocalityService {

    private final LocalityDao localityDao;

    public LocalityService(LocalityDao localityDao) {
        this.localityDao = localityDao;
    }

    public List<LocaliseLocalityDto> getLocaliseLocalities(){

            return localityDao.findAllLocaliseLocalitiesWithoutConnection();
    }


}
