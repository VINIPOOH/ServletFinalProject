package dal.dao;

import dal.entity.Bill;
import exeptions.AskedDataIsNotExist;

import java.util.List;

public interface BillDao {
    boolean createBill(long costInCents, long deliveryId, long UserId);

    List<Bill> getInfoToPayBillByUserId(long UserId);

    long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist;

    public boolean payBill(long userId, long billId);

    List<Bill> getHistoricBailsByUserId(long userId);
}
