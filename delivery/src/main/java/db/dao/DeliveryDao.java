package db.dao;

import exeptions.AskedDataIsNotExist;

public interface DeliveryDao {
    public long createDelivery(String addreeseeEmail, long addresserId, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotExist;

}
