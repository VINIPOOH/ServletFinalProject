package db.dao;

import dto.DeliveryCostAndTimeDto;

import java.util.Optional;

public interface WayDao {
    Optional<DeliveryCostAndTimeDto> findByLocalitySand_IdAndLocalityGet_Id(long localitySandID, long localityGetID, int weight);
}
