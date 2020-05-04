package bll.service.impl;


import bll.dto.LocaliseLocalityDto;
import dal.dao.impl.LocalityDao;

import java.util.List;
import java.util.Locale;

public class LocalityServiceImpl implements bll.service.LocalityService {

    private final LocalityDao localityDao;

    public LocalityServiceImpl(LocalityDao localityDao) {
        this.localityDao = localityDao;
    }

    @Override
    public List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale) {
        return localityDao.findAllLocaliseLocalitiesWithoutConnection(locale);
    }


}
