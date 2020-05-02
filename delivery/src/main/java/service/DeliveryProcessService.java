package service;


import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.WayDao;
import dto.*;
import dal.entity.Delivery;
import exeptions.AskedDataIsNotExist;
import exeptions.FailCreateDeliveryException;
import exeptions.UnsupportableWeightFactorException;
import service.mapper.EntityToDtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<DeliveryCostAndTimeDto> getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) {
        return wayDao.findByLocalitySand_IdAndLocalityGet_Id(deliveryInfoRequestDto.getLocalitySandID()
                ,deliveryInfoRequestDto.getLocalityGetID(),deliveryInfoRequestDto.getDeliveryWeight());
    }

    public boolean initializeDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        long price;
        try {
             price = wayDao.getPrise(deliveryOrderCreateDto.getLocalitySandID()
                    ,deliveryOrderCreateDto.getLocalityGetID(),deliveryOrderCreateDto.getDeliveryWeight());
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            throw new UnsupportableWeightFactorException();
        }
        long newDeliveryId;
        try {
           newDeliveryId = deliveryDao.createDelivery(deliveryOrderCreateDto.getAddresseeEmail(), initiatorId, deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight());
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            throw new FailCreateDeliveryException();
        }
        return billDao.createBill(price,newDeliveryId, initiatorId);
    }

    public List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId){
        List<DeliveryInfoToGetDto> toReturn = new ArrayList<>();
        EntityToDtoMapper<Delivery, DeliveryInfoToGetDto> mapper = (delivery -> DeliveryInfoToGetDto.builder()
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
