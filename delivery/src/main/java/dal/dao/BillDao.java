package dal.dao;

import dal.entity.Bill;
import exeptions.AskedDataIsNotExist;

import java.util.List;

public interface BillDao {
    boolean createBill(long costInCents, long deliveriId, long UserId);

    List<Bill> getInfoToPayBillByUserId(long user_id);

    long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist;

    boolean murkBillAsPayed(long billId);

    List<Bill> getHistoricBailsByUserId(long userId);
}
