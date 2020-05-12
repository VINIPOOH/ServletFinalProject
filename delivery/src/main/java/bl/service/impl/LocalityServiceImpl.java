package bl.service.impl;


import bl.dto.LocaliseLocalityDto;
import bl.dto.mapper.Mapper;
import bl.service.LocalityService;
import dal.dao.LocalityDao;
import dal.entity.Locality;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static bl.service.ServicesConstants.RUSSIAN_LANG_COD;

public class LocalityServiceImpl implements LocalityService {
    private static Logger log = LogManager.getLogger(LocalityServiceImpl.class);

    private final LocalityDao localityDao;

    public LocalityServiceImpl(LocalityDao localityDao) {
        this.localityDao = localityDao;
        log.debug("created");

    }

    @Override
    public List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale) {
        log.debug("localeLang - " + locale.getLanguage());


        return localityDao.findAllLocaliseLocalitiesWithoutConnection(locale).stream()
                .map(getLocalityToLocaliseLocalityDto(locale)::map)
                .collect(Collectors.toList());
    }

    private Mapper<Locality, LocaliseLocalityDto> getLocalityToLocaliseLocalityDto(Locale locale) {
        return locality -> {
            LocaliseLocalityDto toReturn = LocaliseLocalityDto.builder()
                    .id(locality.getId())
                    .build();
            if (locale.getLanguage().equals(RUSSIAN_LANG_COD)) {
                toReturn.setName(locality.getNameRu());
            } else {
                toReturn.setName(locality.getNameEn());
            }
            return toReturn;
        };
    }


}
