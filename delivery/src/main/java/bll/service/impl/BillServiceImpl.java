package bll.service.impl;

import bll.dto.BillDto;
import bll.dto.BillInfoToPayDto;
import bll.dto.mapper.Mapper;
import bll.exeptions.UnsupportableWeightFactorException;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.entity.Bill;
import dal.handling.JDBCDaoSingleton;
import dal.handling.conection.pool.TransactionalManager;
import exeptions.AskedDataIsNotExist;
import exeptions.FailCreateDeliveryException;
import web.dto.DeliveryOrderCreateDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillServiceImpl implements bll.service.BillService {

    private final BillDao billDao;
    private final UserDao userDao;
    private final DeliveryDao deliveryDao;

    public BillServiceImpl(BillDao billDao, UserDao userDao, DeliveryDao deliveryDao) {
        this.billDao = billDao;
        this.userDao = userDao;
        this.deliveryDao = deliveryDao;
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
    public boolean payForDelivery(long userId, long billId) {
        try (TransactionalManager transactionalManager = JDBCDaoSingleton.getTransactionManager()) {
            transactionalManager.startTransaction();
            long billPrise = billDao.getBillCostIfItIsNotPaid(billId, userId);
            boolean a = userDao.replenishUserBalenceOnSumeIfItPosible(userId, billPrise);
            if (a) {
                boolean b = billDao.murkBillAsPayed(billId);
                if (b) {
                    transactionalManager.commit();
                    return true;
                }
            }
            transactionalManager.rollBack();
            return false;
        } catch (SQLException | AskedDataIsNotExist e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        try (TransactionalManager transactionalManager = JDBCDaoSingleton.getTransactionManager()) {
            long newDeliveryId = deliveryDao.createDelivery(deliveryOrderCreateDto.getAddresseeEmail(), initiatorId, deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight());
            if (billDao.createBill(newDeliveryId, initiatorId, deliveryOrderCreateDto.getLocalitySandID()
                    , deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight())) {
                transactionalManager.commit();
                return true;
            }
            transactionalManager.rollBack();
            return false;
        } catch (SQLException | AskedDataIsNotExist e) {
            return false;
        }
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
