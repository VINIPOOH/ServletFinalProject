package bll.service.impl;

import bll.dto.BillDto;
import bll.dto.BillInfoToPayDto;
import bll.dto.mapper.Mapper;
import bll.exeptions.UnsupportableWeightFactorException;
import dal.dao.BillDao;
import dal.dao.UserDao;
import dal.entity.Bill;
import dal.exeptions.DBRuntimeException;
import exeptions.AskedDataIsNotExist;
import exeptions.FailCreateDeliveryException;
import web.dto.DeliveryOrderCreateDto;

import java.util.ArrayList;
import java.util.List;

public class BillServiceImpl implements bll.service.BillService {

    private final BillDao billDao;
    private final UserDao userDao;

    public BillServiceImpl(BillDao billDao, UserDao userDao) {
        this.billDao = billDao;
        this.userDao = userDao;
    }

    @Override
    public List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId) {
        List<BillInfoToPayDto> toReturn = new ArrayList<>();
        Mapper<Bill, BillInfoToPayDto> mapper = bill -> BillInfoToPayDto.builder()
                .weight(bill.getDelivery().getWeight())
                .price(bill.getCostInCents())
                .localitySandName(bill.getDelivery().getWay().getLocalitySand().getNameEn())
                .localityGetName(bill.getDelivery().getWay().getLocalityGet().getNameEn())
                .deliveryId(bill.getDelivery().getId())
                .billId(bill.getId())
                .addreeserEmail(bill.getDelivery().getAddresser().getEmail())
                .build();
        for (Bill b : billDao.getInfoToPayBillByUserId(userId)) {
            toReturn.add(mapper.map(b));
        }
        return toReturn;
    }

    @Override
    public void payForDelivery(long userId, long billId) {
        billDao.payBill(userId,billId);
    }

    @Override
    public boolean initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        return billDao.initializeDelivery(deliveryOrderCreateDto,initiatorId);
    }

    @Override
    public List<BillDto> getBillHistoryByUserId(long userId) {
        List<BillDto> toReturn = new ArrayList<>();
        Mapper<Bill, BillDto> mapper = bill -> BillDto.builder()
                .id(bill.getId())
                .deliveryId(bill.getDelivery().getId())
                .isDeliveryPaid(bill.getIsDeliveryPaid())
                .costInCents(bill.getCostInCents())
                .dateOfPay(bill.getDateOfPay())
                .build();
        for (Bill b : billDao.getHistoricBailsByUserId(userId)) {
            toReturn.add(mapper.map(b));
        }
        return toReturn;
    }
}
