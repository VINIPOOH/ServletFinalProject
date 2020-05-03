package dal.dao;

import dal.entity.Delivery;
import exeptions.AskedDataIsNotExist;

import java.util.List;

public interface DeliveryDao {
    long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist;

    List<Delivery> getDeliveryInfoToGet(long userId);

    void confirmGettingDelivery(long userId, long deliveryId);
}
