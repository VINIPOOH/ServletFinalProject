package bll.service;

import bll.dto.BillDto;
import bll.dto.BillInfoToPayDto;
import bll.exeptions.UnsupportableWeightFactorException;
import dal.entity.Locality;
import exeptions.FailCreateDeliveryException;
import web.dto.DeliveryOrderCreateDto;

import java.util.List;
import java.util.Locale;

public interface BillService {
    List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId, Locale locale);

    boolean payForDelivery(long userId, long billId);

    boolean initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException;

    List<BillDto> getBillHistoryByUserId(long userId);
}
