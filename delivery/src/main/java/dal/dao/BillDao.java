package dal.dao;

import dal.entity.Bill;
import dal.exeptions.AskedDataIsNotCorrect;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public interface BillDao {

    List<Bill> getInfoToPayBillByUserId(long userId, Locale locale);

    long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotCorrect;


    List<Bill> getHistoricBillsByUserId(long userId, Integer offset, Integer limit);

    boolean murkBillAsPayed(long billId) throws SQLException;

    boolean createBill(long deliveryId, long userId, long localitySandID, long localityGetID, int weight) throws SQLException;

    long countAllBillsByUserId(long userId);
}
