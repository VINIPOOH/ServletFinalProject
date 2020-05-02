package dal.dao;

import dal.entity.Delivery;
import exeptions.AskedDataIsNotExist;

import java.util.List;

public interface DeliveryDao {
    public long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist;

    public List<Delivery> getDeliveryInfoToGet(long userId);

    public void confirmGettingDelivery(long userId,long deliveryId);
}
