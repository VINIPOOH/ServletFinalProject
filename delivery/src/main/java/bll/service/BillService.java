package bll.service;

import bll.dto.BillDto;
import bll.dto.BillInfoToPayDto;
import bll.exeptions.UnsupportableWeightFactorException;
import exeptions.FailCreateDeliveryException;
import web.dto.DeliveryOrderCreateDto;

import java.util.List;

public interface BillService {
    List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId);

    void payForDelivery(long userId, long billId);

    boolean initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException;

    List<BillDto> getBillHistoryByUserId(long userId);
}
