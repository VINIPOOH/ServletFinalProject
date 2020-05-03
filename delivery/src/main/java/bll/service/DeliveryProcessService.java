package bll.service;


import dal.JDBCDaoHolder;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.WayDao;
import bll.dto.*;
import dal.dao.conection.TransactionConnectionHandler;
import dal.dto.DeliveryCostAndTimeDto;
import dal.entity.Delivery;
import dal.exeptions.NoConnectionToDbOrConnectionIsAlreadyClosedException;
import exeptions.AskedDataIsNotExist;
import exeptions.DBRuntimeException;
import exeptions.FailCreateDeliveryException;
import exeptions.UnsupportableWeightFactorException;
import bll.service.mapper.Mapper;
import web.dto.DeliveryInfoRequestDto;
import web.dto.DeliveryOrderCreateDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryProcessService {
//    private final UserDao userDao;
    private final WayDao wayDao;
    private final DeliveryDao deliveryDao;
    private final BillDao billDao;

    public DeliveryProcessService(WayDao wayDao, DeliveryDao deliveryDao, BillDao billDao) {
        this.wayDao = wayDao;
        this.deliveryDao = deliveryDao;
        this.billDao = billDao;
    }

    //    public Page<Delivery> findDeliveryHistoryByUserId(long userId, Pageable pageable) {
//        return deliveryDao.findAllByAddressee_IdOrAddresser_Id(userId, userId, pageable);
//    }

//    public List<Delivery> getPricesAndNotTakenDeliversByUserId(long userId) {
//        return deliveryDao.findAllByIsPackageReceivedFalseAndIsDeliveryPaidTrueAndAddressee_Id(userId);
//    }

//    @Transactional
//    public void confirmGettingDelivery(long deliveryId) throws AskedDataIsNotExist {
//        Delivery deliveryToUpdate = deliveryDao.findById(deliveryId).orElseThrow(AskedDataIsNotExist::new);
//        deliveryToUpdate.setIsPackageReceived(true);
//        deliveryDao.save(deliveryToUpdate);
//    }

//    public List<Delivery> getNotPayedDeliversByUserId(long userId) {
//        return deliveryDao.findAllByIsDeliveryPaidFalseAndAddresser_Id(userId);
//    }

//    @Transactional
//    public Delivery payForDelivery(long deliveryId, long userId) throws AskedDataIsNotExist, DeliveryAlreadyPaidException, NoSuchUserException, NotEnoughMoneyException {
//        Delivery deliveryToUpdate = getDeliveryOrException(deliveryId);
//        UserProfile user = getUserOrException(userId, deliveryToUpdate);
//        return deliveryDao.save(prepareDeliverySaveData(deliveryToUpdate, user));
//    }

//    private Delivery prepareDeliverySaveData(Delivery deliveryToUpdate, UserProfile user) {
//        user.setUserMoneyInCents(user.getUserMoneyInCents() - deliveryToUpdate.getCostInCents());
//        deliveryToUpdate.setIsDeliveryPaid(true);
//        deliveryToUpdate.setAddresser(user);
//        deliveryToUpdate.setArrivalDate(LocalDate.now().plusDays(deliveryToUpdate.getWay().getTimeOnWayInDays()));
//        return deliveryToUpdate;
//    }

    //lamda
//    private UserProfile getUserOrException(long userId, Delivery deliveryToUpdate) throws NoSuchUserException, NotEnoughMoneyException {
//        UserProfile user = userDao.findById(userId).orElseThrow(NoSuchUserException::new);
//        if (user.getUserMoneyInCents() < deliveryToUpdate.getCostInCents()) {
//            throw new NotEnoughMoneyException();
//        }
//        return user;
//    }
//
//    private Delivery getDeliveryOrException(long deliveryId) throws AskedDataIsNotExist, DeliveryAlreadyPaidException {
//        Delivery deliveryToUpdate = deliveryDao.findById(deliveryId).orElseThrow(AskedDataIsNotExist::new);
//        if (deliveryToUpdate.getIsDeliveryPaid()) {
//            throw new DeliveryAlreadyPaidException();
//        }
//        return deliveryToUpdate;
//    }

//    @Transactional
//    public void createDeliveryOrder(DeliveryOrderCreateDto deliveryOrderCreateDto) throws NoSuchUserException, NoSuchWayException, UnsupportableWeightFactorException {
//        deliveryDao.save(buildDelivery(deliveryOrderCreateDto,
//                getWay(deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID())));
//    }

    public PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws AskedDataIsNotExist {
        Mapper<DeliveryCostAndTimeDto, PriceAndTimeOnDeliveryDto> mapper = (deliveryCostAndTime)-> PriceAndTimeOnDeliveryDto.builder()
        .costInCents(deliveryCostAndTime.getCostInCents())
                .timeOnWayInHours(deliveryCostAndTime.getTimeOnWayInHours())
                .build();

        return mapper.map(wayDao.findByLocalitySand_IdAndLocalityGet_Id(deliveryInfoRequestDto.getLocalitySandID()
                ,deliveryInfoRequestDto.getLocalityGetID(),deliveryInfoRequestDto.getDeliveryWeight()).orElseThrow(AskedDataIsNotExist::new));
    }

    public boolean initializeDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        TransactionConnectionHandler transactionHandler = JDBCDaoHolder.getConnectionPullHolderForTransaction();
        WayDao wayDtoTransactional = JDBCDaoHolder.getTransactionalWayDao(transactionHandler);
        DeliveryDao deliveryDaoTrTransactional = JDBCDaoHolder.getTransactionalDeliveryDao(transactionHandler);
        BillDao billDaoTransactional = JDBCDaoHolder.getTransactionalBillDao(transactionHandler);

        try {
            transactionHandler.peeperToTransaction();
            long price= wayDtoTransactional.getPrise(deliveryOrderCreateDto.getLocalitySandID()
                    ,deliveryOrderCreateDto.getLocalityGetID(),deliveryOrderCreateDto.getDeliveryWeight());
            long newDeliveryId = deliveryDaoTrTransactional.createDelivery(deliveryOrderCreateDto.getAddresseeEmail(), initiatorId, deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight());
            transactionHandler.commitTransaction();
            return billDaoTransactional.createBill(price,newDeliveryId, initiatorId);
        } catch (NoConnectionToDbOrConnectionIsAlreadyClosedException e) {
            e.printStackTrace();
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            askedDataIsNotExist.printStackTrace();
        }
        throw new DBRuntimeException();
    }

    public List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId){
        List<DeliveryInfoToGetDto> toReturn = new ArrayList<>();
        Mapper<Delivery, DeliveryInfoToGetDto> mapper = (delivery -> DeliveryInfoToGetDto.builder()
                .addresserEmail(delivery.getAddresser().getEmail())
                .deliveryId(delivery.getId())
                .localitySandName(delivery.getWay().getLocalitySand().getNameEn())
                .localityGetName(delivery.getWay().getLocalityGet().getNameEn())
                .build());
        for (Delivery d: deliveryDao.getDeliveryInfoToGet(userId) ){
            toReturn.add(mapper.map(d));
        }
        return toReturn;
    }

    public void ConfirmGetingDelivery(long userId, long deliveryId){
        deliveryDao.confirmGettingDelivery(userId, deliveryId);
    }

}
