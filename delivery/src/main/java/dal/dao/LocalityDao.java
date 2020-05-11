package dal.dao;

import dal.entity.Locality;
import dal.entity.User;
import dal.exeptions.AskedDataIsNotCorrect;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface LocalityDao {


    List<Locality> findAllLocaliseLocalitiesWithoutConnection(Locale locale);
}
