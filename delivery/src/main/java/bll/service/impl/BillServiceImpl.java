package bll.service.impl;

import bll.dto.BillDto;
import bll.dto.BillInfoToPayDto;
import bll.dto.mapper.Mapper;
import bll.exeptions.AskedDataIsNotExist;
import bll.exeptions.FailCreateDeliveryException;
import bll.exeptions.UnsupportableWeightFactorException;
import bll.service.BillService;
import dal.control.JDBCDaoContext;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.entity.Bill;
import web.dto.DeliveryOrderCreateDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BillServiceImpl implements BillService {

    private final BillDao billDao;
    private final UserDao userDao;
    private final DeliveryDao deliveryDao;

    public BillServiceImpl(BillDao billDao, UserDao userDao, DeliveryDao deliveryDao) {
        this.billDao = billDao;
        this.userDao = userDao;
        this.deliveryDao = deliveryDao;
    }

    @Override
    public List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId, Locale locale) {
        return billDao.getInfoToPayBillByUserId(userId, locale).stream()
                .map(getMapperBillInfoToPayDto(locale)::map)
                .collect(Collectors.toList());
    }


    @Override
    public boolean payForDelivery(long userId, long billId) {
        try (TransactionalManager transactionalManager = JDBCDaoContext.getTransactionManager()) {
            transactionalManager.startTransaction();
            if (userDao.replenishUserBalenceOnSumeIfItPosible(userId,
                    billDao.getBillCostIfItIsNotPaid(billId, userId))
                    && billDao.murkBillAsPayed(billId)) {
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
    public void initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        try (TransactionalManager transactionalManager = JDBCDaoContext.getTransactionManager()) {
            transactionalManager.startTransaction();
            long newDeliveryId = deliveryDao.createDelivery(deliveryOrderCreateDto.getAddresseeEmail(), deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight());
            if (billDao.createBill(newDeliveryId, initiatorId, deliveryOrderCreateDto.getLocalitySandID()
                    , deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight())) {
                transactionalManager.commit();
                return;
            }
            transactionalManager.rollBack();
            throw new UnsupportableWeightFactorException();
        } catch (SQLException | AskedDataIsNotExist e) {
            throw new FailCreateDeliveryException();
        }
    }

    @Override
    public List<BillDto> getBillHistoryByUserId(long userId) {
        return billDao.getHistoricBailsByUserId(userId).stream()
                .map(getBillBillDtoMapper()::map)
                .collect(Collectors.toList());
    }

    private Mapper<Bill, BillInfoToPayDto> getMapperBillInfoToPayDto(Locale locale) {
        return bill -> {
            BillInfoToPayDto billInfoToPayDto = BillInfoToPayDto.builder()
                    .weight(bill.getDelivery().getWeight())
                    .price(bill.getCostInCents())
                    .deliveryId(bill.getDelivery().getId())
                    .billId(bill.getId())
                    .addreeserEmail(bill.getDelivery().getAddressee().getEmail())
                    .build();
            if (locale.getLanguage().equals("ru")) {
                billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameRu());
                billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameRu());
            } else {
                billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameEn());
                billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameEn());
            }
            return billInfoToPayDto;
        };
    }

    private Mapper<Bill, BillDto> getBillBillDtoMapper() {
        return bill -> BillDto.builder()
                .id(bill.getId())
                .deliveryId(bill.getDelivery().getId())
                .isDeliveryPaid(bill.getIsDeliveryPaid())
                .costInCents(bill.getCostInCents())
                .dateOfPay(bill.getDateOfPay())
                .build();
    }
}
