package bll.service.impl;

import bll.dto.BillDto;
import bll.dto.BillInfoToPayDto;
import bll.dto.mapper.Mapper;
import bll.exeptions.UnsupportableWeightFactorException;
import dal.handling.JDBCDaoSingleton;
import dal.dao.BillDao;
import dal.handling.transaction.TransactionManager;
import dal.entity.Bill;
import exeptions.AskedDataIsNotExist;
import exeptions.FailCreateDeliveryException;
import web.dto.DeliveryOrderCreateDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillServiceImpl implements bll.service.BillService {

    private final BillDao billDao;

    public BillServiceImpl(BillDao billDao) {
        this.billDao = billDao;
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

        try (TransactionManager transactionManager = JDBCDaoSingleton.getTransactionManager()) {
            long billPrise = JDBCDaoSingleton.getBillDaoForTransaction(transactionManager).getBillCostIfItIsNotPaid(billId, userId);
            boolean a = JDBCDaoSingleton.getUserDaoForTransaction(transactionManager).replenishUserBalenceOnSumeIfItPosible(userId, billPrise);
            if (a) {
                boolean b = JDBCDaoSingleton.getBillDaoForTransaction(transactionManager).murkBillAsPayed(billId);
                if (b) {
                    transactionManager.commit();
                    return true;
                }
            }
            transactionManager.rollBack();
            return false;
        } catch (SQLException | AskedDataIsNotExist e) {
            return false;
        }
    }

    @Override
    public boolean initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        try (TransactionManager transactionManager = JDBCDaoSingleton.getTransactionManager()) {
            long newDeliveryId = JDBCDaoSingleton.getDeliveryForTransaction(transactionManager).createDelivery(deliveryOrderCreateDto.getAddresseeEmail(), initiatorId, deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight());
            if (JDBCDaoSingleton.getBillDaoForTransaction(transactionManager).createBill(newDeliveryId, initiatorId, deliveryOrderCreateDto.getLocalitySandID()
                    , deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight())) {
                transactionManager.commit();
                return true;
            }
            transactionManager.rollBack();
            return false;
        } catch (SQLException | AskedDataIsNotExist e) {
            return false;
        }
    }

        @Override
        public List<BillDto> getBillHistoryByUserId ( long userId){
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
