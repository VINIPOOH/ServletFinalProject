package service;


import db.dao.UserDao;
import db.dao.WayDao;
import db.dao.impl.DeliveryDao;
import db.dao.impl.JDBCWayDao;
import dto.DeliveryCostAndTimeDto;
import dto.DeliveryInfoRequestDto;
import exeptions.NoSuchWayException;
import exeptions.UnsupportableWeightFactorException;

import java.util.Optional;

public class DeliveryProcessService {
//    private final UserDao userDao;
    private final WayDao wayDao;
//    private final DeliveryDao deliveryDao;

    public DeliveryProcessService(WayDao wayDao) {
        this.wayDao = wayDao;
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

    //разобраться с етой транзакцией (как вариант дабл чек)
//    private Delivery buildDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, Way way) throws NoSuchUserException, UnsupportableWeightFactorException {
//        return Delivery.builder()
//                .addressee(userDao.findByEmail(deliveryOrderCreateDto.getAddresseeEmail()).orElseThrow(NoSuchUserException::new))
//                .addresser(userDao.findByEmail(deliveryOrderCreateDto.getAddresserEmail()).orElseThrow(NoSuchUserException::new))
//                .way(way)
//                .isPackageReceived(false)
//                .weight(deliveryOrderCreateDto.getDeliveryWeight())
//                .isDeliveryPaid(false)
//                .costInCents(calculateDeliveryCost(deliveryOrderCreateDto.getDeliveryWeight(), way))
//                .build();
//    }
//

//
//    private int calculateDeliveryCost(int deliveryWeight, Way way) throws UnsupportableWeightFactorException {
//        int overPayOnKilometerForWeight = way.getWayTariffs().stream()
//                .filter(x -> x.getMinWeightRange() <= deliveryWeight
//                        && x.getMaxWeightRange() > deliveryWeight)
//                .findFirst().orElseThrow(UnsupportableWeightFactorException::new)
//                .getOverPayOnKilometer();
//        return (overPayOnKilometerForWeight + way.getPriceForKilometerInCents()) * way.getDistanceInKilometres();
//    }

}
