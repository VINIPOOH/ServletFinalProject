package dal.dao;

import dal.dto.DeliveryCostAndTimeDto;

import java.util.Optional;

public interface WayDao {
    Optional<DeliveryCostAndTimeDto> findByLocalitySandIdAndLocalityGetId(long localitySandID, long localityGetID, int weight);


}