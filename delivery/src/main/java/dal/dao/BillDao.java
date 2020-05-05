package dal.dao;

import dal.entity.Bill;
import exeptions.AskedDataIsNotExist;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public interface BillDao {

    List<Bill> getInfoToPayBillByUserId(long userId, Locale locale);

    long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist;

    List<Bill> getHistoricBailsByUserId(long userId);

    boolean murkBillAsPayed(long billId) throws SQLException;

    boolean createBill(long deliveryId, long userId, long localitySandID, long localityGetID, int weight) throws SQLException;
}
