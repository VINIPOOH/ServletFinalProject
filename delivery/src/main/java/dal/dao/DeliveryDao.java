package dal.dao;

import dal.entity.Delivery;
import exeptions.AskedDataIsNotExist;

import java.util.List;

public interface DeliveryDao {

    List<Delivery> getDeliveryInfoToGet(long userId);

    void confirmGettingDelivery(long userId, long deliveryId);
}
