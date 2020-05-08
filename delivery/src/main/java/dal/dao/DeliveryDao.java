package dal.dao;

import bll.exeptions.AskedDataIsNotExist;
import dal.entity.Delivery;

import java.util.List;
import java.util.Locale;

public interface DeliveryDao {

    List<Delivery> getDeliveryInfoToGet(long userId, Locale locale);

    void confirmGettingDelivery(long userId, long deliveryId);

    long createDelivery(String addreeseeEmail, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist;

}
