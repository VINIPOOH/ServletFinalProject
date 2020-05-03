package bll.service;

import bll.dto.BillDto;
import bll.dto.BillInfoToPayDto;

import java.util.List;

public interface BillService {
    List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId);

    void payForDelivery(long userId, long billId);

    List<BillDto> getBillHistoryByUserId(long userId);
}
