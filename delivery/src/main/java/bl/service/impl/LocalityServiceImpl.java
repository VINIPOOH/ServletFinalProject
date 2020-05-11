package bl.service.impl;


import bl.dto.LocaliseLocalityDto;
import bl.service.LocalityService;
import dal.dao.impl.LocalityDao;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;

public class LocalityServiceImpl implements LocalityService {
    private static Logger log = LogManager.getLogger(LocalityServiceImpl.class);

    private final LocalityDao localityDao;

    public LocalityServiceImpl(LocalityDao localityDao) {
        log.debug("created");

        this.localityDao = localityDao;
    }

    @Override
    public List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale) {
        log.debug("localeLang - " + locale.getLanguage());

        return localityDao.findAllLocaliseLocalitiesWithoutConnection(locale);
    }


}