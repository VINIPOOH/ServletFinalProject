package dal.dao;

import dal.entity.Delivery;
import bll.exeptions.AskedDataIsNotExist;

import java.util.List;
import java.util.Locale;

public interface DeliveryDao {

    List<Delivery> getDeliveryInfoToGet(long userId, Locale locale);

    void confirmGettingDelivery(long userId, long deliveryId);

    long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist;

}
