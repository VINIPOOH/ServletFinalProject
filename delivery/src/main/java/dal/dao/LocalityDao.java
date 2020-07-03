package dal.dao;

import dal.entity.Locality;

import java.util.List;
import java.util.Locale;

public interface LocalityDao {


    List<Locality> findAllLocaliseLocalitiesWithoutConnection(Locale locale);

    List<Locality> findLocaliseLocalitiesGetByLocalitySendId(Locale locale, long id);

}
