package service;


import db.dao.impl.LocalityDao;
import dto.LocaliseLocalityDto;

import java.util.List;
import java.util.Locale;

public class LocalityService {

    private final LocalityDao localityDao;

    public LocalityService(LocalityDao localityDao) {
        this.localityDao = localityDao;
    }

    public List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale){
            return localityDao.findAllLocaliseLocalitiesWithoutConnection(locale);
    }


}
