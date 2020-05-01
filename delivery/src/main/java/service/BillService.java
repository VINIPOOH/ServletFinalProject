package service;

import db.dao.BillDao;
import db.dao.UserDao;
import dto.BillDto;
import dto.BillInfoToPayDto;
import exeptions.AskedDataIsNotExist;
import exeptions.DBRuntimeException;

import java.util.List;

public class BillService {

    private final BillDao billDao;
    private final UserDao userDao;

    public BillService(BillDao billDao, UserDao userDao) {
        this.billDao = billDao;
        this.userDao = userDao;
    }

    public List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId){
        return billDao.getInfoToPayBillByUserId(userId);
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
    return billDao.getHistoricBailsByUserId(userId);
    }
}
