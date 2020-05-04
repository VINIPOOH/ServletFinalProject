package dal.dao;

import dal.entity.Bill;
import exeptions.AskedDataIsNotExist;
import web.dto.DeliveryOrderCreateDto;

import java.sql.SQLException;
import java.util.List;

public interface BillDao {

    List<Bill> getInfoToPayBillByUserId(long UserId);

    long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist;

    List<Bill> getHistoricBailsByUserId(long userId);
    public long getBillPrice(long userId, long billId) throws SQLException, AskedDataIsNotExist;
    public boolean murkBillAsPayed(long billId) throws SQLException;
    public boolean createBill(long deliveryId, long userId,long localitySandID, long localityGetID, int weight) throws SQLException;
}
