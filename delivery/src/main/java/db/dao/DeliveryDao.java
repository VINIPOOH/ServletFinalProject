package db.dao;

import dto.DeliveryInfoRequestDto;
import dto.DeliveryInfoToGetDto;
import exeptions.AskedDataIsNotExist;

import java.util.List;

public interface DeliveryDao {
    public long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist;

    public List<DeliveryInfoToGetDto> getDeliveryInfoToGet(long userId);

    public void confirmGettingDelivery(long userId,long deliveryId);
}
