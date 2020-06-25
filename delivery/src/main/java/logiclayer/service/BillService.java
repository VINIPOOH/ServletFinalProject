package logiclayer.service;

import dto.BillDto;
import dto.BillInfoToPayDto;
import dto.DeliveryOrderCreateDto;
import logiclayer.exeption.FailCreateDeliveryException;
import logiclayer.exeption.OperationFailException;
import logiclayer.exeption.UnsupportableWeightFactorException;

import java.util.List;
import java.util.Locale;

public interface BillService {
    List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId, Locale locale);

    void payForDelivery(long userId, long billId) throws OperationFailException;

    long countAllBillsByUserId(long userId);

    void initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException;

    List<BillDto> getBillHistoryByUserId(long userId, int offset, int limit);
}
