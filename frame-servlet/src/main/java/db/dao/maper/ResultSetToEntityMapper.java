package db.dao.maper;

import java.sql.ResultSet;
import java.util.Optional;

@FunctionalInterface
public interface ResultSetToEntityMapper <E> {
    Optional<E> map(ResultSet resultSet);
}
