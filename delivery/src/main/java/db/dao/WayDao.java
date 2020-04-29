package db.dao;

import dto.DeliveryCostAndTimeDto;
import exeptions.AskedDataIsNotExist;

import java.util.Optional;

public interface WayDao {
    Optional<DeliveryCostAndTimeDto> findByLocalitySand_IdAndLocalityGet_Id(long localitySandID, long localityGetID, int weight);

    public long getPrise(long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist;

}