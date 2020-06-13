package logiclayer.service.impl;

import dal.conection.pool.TransactionalManager;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.entity.Bill;
import dal.exeption.AskedDataIsNotCorrect;
import dto.BillDto;
import dto.BillInfoToPayDto;
import dto.DeliveryOrderCreateDto;
import dto.mapper.Mapper;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.exeption.FailCreateDeliveryException;
import logiclayer.exeption.UnsupportableWeightFactorException;
import logiclayer.service.BillService;
import logiclayer.service.ServicesConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Singleton
@NeedConfig
public class BillServiceImpl implements BillService {
    private static final Logger log = LogManager.getLogger(BillServiceImpl.class);
    @InjectByType
    private
    TransactionalManager transactionalManager;
    @InjectByType
    private BillDao billDao;
    @InjectByType
    private UserDao userDao;
    @InjectByType
    private DeliveryDao deliveryDao;

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

        try {
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
        } finally {
            transactionalManager.close();
        }

    }

    @Override
    public long countAllBillsByUserId(long userId) {
        return billDao.countAllBillsByUserId(userId);
    }

    @Override
    public boolean initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        log.debug("deliveryOrderCreateDto - " + deliveryOrderCreateDto + " initiatorId - " + initiatorId);

        try {
            transactionalManager.startTransaction();
            long newDeliveryId = deliveryDao.createDelivery(deliveryOrderCreateDto.getAddresseeEmail(), deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight());
            if (billDao.createBill(newDeliveryId, initiatorId, deliveryOrderCreateDto.getLocalitySandID()
                    , deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight())) {
                transactionalManager.commit();
                return true;
            }
            transactionalManager.rollBack();
            throw new UnsupportableWeightFactorException();
        } catch (SQLException e) {
            log.error("problem with db", e);
            throw new FailCreateDeliveryException();
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            log.error("askedDataIsNotCorrect", askedDataIsNotCorrect);
            throw new FailCreateDeliveryException();
        } finally {
            transactionalManager.close();
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
        return bill -> BillInfoToPayDto.builder()
                .weight(bill.getDelivery().getWeight())
                .price(bill.getCostInCents())
                .deliveryId(bill.getDelivery().getId())
                .billId(bill.getId())
                .addreeseeEmail(bill.getDelivery().getAddressee().getEmail())
                .localitySandName(locale.getLanguage().equals(ServicesConstants.RUSSIAN_LANG_COD) ?
                        bill.getDelivery().getWay().getLocalitySand().getNameRu() :
                        bill.getDelivery().getWay().getLocalitySand().getNameEn())
                .localityGetName(locale.getLanguage().equals(ServicesConstants.RUSSIAN_LANG_COD) ?
                        bill.getDelivery().getWay().getLocalityGet().getNameRu() :
                        bill.getDelivery().getWay().getLocalityGet().getNameEn())
                .build();
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
