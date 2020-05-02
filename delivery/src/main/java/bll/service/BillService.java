package bll.service;

import dal.dao.BillDao;
import dal.dao.UserDao;
import bll.dto.BillDto;
import bll.dto.BillInfoToPayDto;
import dal.entity.Bill;
import exeptions.AskedDataIsNotExist;
import exeptions.DBRuntimeException;
import bll.service.mapper.Mapper;

import java.util.ArrayList;
import java.util.List;

public class BillService {

    private final BillDao billDao;
    private final UserDao userDao;

    public BillService(BillDao billDao, UserDao userDao) {
        this.billDao = billDao;
        this.userDao = userDao;
    }

    public List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId){
        List<BillInfoToPayDto> toReturn = new ArrayList<>();
        Mapper<Bill, BillInfoToPayDto> mapper = (bill)-> BillInfoToPayDto.builder()
                .weight(bill.getDelivery().getWeight())
                .price(bill.getCostInCents())
                .localitySandName(bill.getDelivery().getWay().getLocalitySand().getNameEn())
                .localityGetName(bill.getDelivery().getWay().getLocalityGet().getNameEn())
                .delivery_id(bill.getDelivery().getId())
                .bill_id(bill.getId())
                .addreeserEmail(bill.getDelivery().getAddresser().getEmail())
                .build();
        for (Bill b : billDao.getInfoToPayBillByUserId(userId) ){
            toReturn.add(mapper.map(b));
        }
        return toReturn;
    }

    public void payForDelivery(long userId, long billId){

        long billPrise;
        try {
            billPrise = billDao.getBillCostIfItIsNotPaid(billId, userId);
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            askedDataIsNotExist.printStackTrace();
            throw new DBRuntimeException();
        }
        if (userDao.replenishUserBalenceOnSumeIfItPosible(userId, billPrise)){
            billDao.murkBillAsPayed(billId);
        }

    }

    public List<BillDto> getBillHistoryByUserId(long userId){
        List<BillDto> toReturn = new ArrayList<>();
        Mapper<Bill, BillDto> mapper = (bill)-> BillDto.builder()
                .id(bill.getId())
                .deliveryId(bill.getDelivery().getId())
                .isDeliveryPaid(bill.getIsDeliveryPaid())
                .costInCents(bill.getCostInCents())
                .dateOfPay(bill.getDateOfPay())
                .build();
        for (Bill b : billDao.getHistoricBailsByUserId(userId) ){
            toReturn.add(mapper.map(b));
        }
        return toReturn;
    }
}
