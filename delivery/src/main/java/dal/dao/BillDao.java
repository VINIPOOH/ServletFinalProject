package dal.dao;

import dal.entity.Bill;
import exeptions.AskedDataIsNotExist;

import java.util.List;

public interface BillDao {
    boolean createBill(long costInCents, long deliveriId, long UserId);

    public List<Bill> getInfoToPayBillByUserId(long user_id);

    public long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist;

    public boolean murkBillAsPayed(long billId);

    public List<Bill> getHistoricBailsByUserId(long userId);
}
