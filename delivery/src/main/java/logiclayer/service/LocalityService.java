package logiclayer.service;

import dto.LocaliseLocalityDto;

import java.util.List;
import java.util.Locale;

public interface LocalityService {
    List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale);
    List<LocaliseLocalityDto> getLocaliseLocalitiesGetByLocalitySendId(Locale locale, long id);
}
