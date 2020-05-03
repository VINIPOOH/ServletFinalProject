package bll.service;

import bll.dto.LocaliseLocalityDto;

import java.util.List;
import java.util.Locale;

public interface LocalityService {
    List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale);
}
