package bl.service.impl;

import bl.dto.BillDto;
import bl.dto.BillInfoToPayDto;
import bl.dto.mapper.Mapper;
import bl.exeptions.FailCreateDeliveryException;
import bl.exeptions.UnsupportableWeightFactorException;
import bl.service.BillService;
import dal.control.JDBCDaoContext;
import dal.control.conection.pool.TransactionalManager;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.entity.Bill;
import dal.exeptions.AskedDataIsNotCorrect;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.dto.DeliveryOrderCreateDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static bl.service.ServicesConstants.RUSSIAN_LANG_COD;

public class BillServiceImpl implements BillService {
    private static Logger log = LogManager.getLogger(BillServiceImpl.class);

    private final BillDao billDao;
    private final UserDao userDao;
    private final DeliveryDao deliveryDao;

    public BillServiceImpl(BillDao billDao, UserDao userDao, DeliveryDao deliveryDao) {
        log.debug("created");

        this.billDao = billDao;
        this.userDao = userDao;
        this.deliveryDao = deliveryDao;
    }

    @Override
    public List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId, Locale locale) {
        log.debug("userId - " + userId + " localeLang - " + locale.getLanguage());

        return billDao.getInfoToPayBillByUserId(userId, locale).stream()
                .map(getMapperBillInfoToPayDto(locale)::map)
                .collect(Collectors.toList());
    }


    @Override
    public boolean payForDelivery(long userId, long billId) {
        log.debug("userId - " + userId + " billId - " + billId);

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
        } catch (SQLException e) {
            log.error("problem with db", e);
            return false;
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            log.error("askedDataIsNotCorrect", askedDataIsNotCorrect);
            return false;
        }

    }

    @Override
    public long countAllBillsBiUserId(long userId) {
        return billDao.countAllBillsByUserId(userId);
    }

    @Override
    public void initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        log.debug("deliveryOrderCreateDto - " + deliveryOrderCreateDto + " initiatorId - " + initiatorId);

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
        } catch (SQLException e) {
            log.error("problem with db", e);
            throw new FailCreateDeliveryException();
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            log.error("askedDataIsNotCorrect", askedDataIsNotCorrect);
            askedDataIsNotCorrect.printStackTrace();
        }
    }

    @Override
    public List<BillDto> getBillHistoryByUserId(long userId, int offset, int limit) {
        log.debug("userId - " + userId);

        return billDao.getHistoricBillsByUserId(userId, offset, limit).stream()
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
            if (locale.getLanguage().equals(RUSSIAN_LANG_COD)) {
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
