package dal.dao;

import dal.entity.Bill;
import exeptions.AskedDataIsNotExist;
import web.dto.DeliveryOrderCreateDto;

import java.util.List;

public interface BillDao {
    public boolean initializeDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId);

    List<Bill> getInfoToPayBillByUserId(long UserId);

    long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotExist;

    boolean payBill(long userId, long billId);

    List<Bill> getHistoricBailsByUserId(long userId);
}
