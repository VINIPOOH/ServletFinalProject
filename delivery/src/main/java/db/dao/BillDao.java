package db.dao;

import dto.BillDto;
import dto.BillInfoToPayDto;
import exeptions.AskedDataIsNotExist;

import java.util.List;

public interface BillDao {
    boolean createBill(long costInCents, long deliveriId, long UserId);

    public List<BillInfoToPayDto> getInfoToPayBillByUserId(long user_id);

    public long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist;

    public boolean murkBillAsPayed(long billId);

    public List<BillDto> getHistoricBailsByUserId(long userId);
}
