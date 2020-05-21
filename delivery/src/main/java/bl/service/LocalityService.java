package bl.service;

import dto.LocaliseLocalityDto;

import java.util.List;
import java.util.Locale;

public interface LocalityService {
    List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale);
}
