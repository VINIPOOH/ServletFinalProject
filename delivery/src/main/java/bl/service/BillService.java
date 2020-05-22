package bl.service;

import bl.exeption.FailCreateDeliveryException;
import bl.exeption.UnsupportableWeightFactorException;
import dto.BillDto;
import dto.BillInfoToPayDto;
import dto.DeliveryOrderCreateDto;

import java.util.List;
import java.util.Locale;

public interface BillService {
    List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId, Locale locale);

    boolean payForDelivery(long userId, long billId);

    long countAllBillsByUserId(long userId);

    boolean initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException;

    List<BillDto> getBillHistoryByUserId(long userId, int offset, int limit);
}
