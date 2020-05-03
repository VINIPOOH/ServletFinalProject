package bll.service.impl;


import dal.dao.impl.LocalityDao;
import bll.dto.LocaliseLocalityDto;

import java.util.List;
import java.util.Locale;

public class LocalityService implements bll.service.LocalityService {

    private final LocalityDao localityDao;

    public LocalityService(LocalityDao localityDao) {
        this.localityDao = localityDao;
    }

    @Override
    public List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale){
            return localityDao.findAllLocaliseLocalitiesWithoutConnection(locale);
    }


}
