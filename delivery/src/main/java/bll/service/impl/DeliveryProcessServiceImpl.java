package bll.service.impl;


import bll.dto.DeliveryInfoToGetDto;
import bll.dto.PriceAndTimeOnDeliveryDto;
import bll.dto.mapper.Mapper;
import bll.exeptions.UnsupportableWeightFactorException;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.WayDao;
import dal.dto.DeliveryCostAndTimeDto;
import dal.entity.Delivery;
import exeptions.AskedDataIsNotExist;
import exeptions.FailCreateDeliveryException;
import web.dto.DeliveryInfoRequestDto;
import web.dto.DeliveryOrderCreateDto;

import java.util.ArrayList;
import java.util.List;

public class DeliveryProcessServiceImpl implements bll.service.DeliveryProcessService {

    private final WayDao wayDao;
    private final DeliveryDao deliveryDao;
    private final BillDao billDao;

    public DeliveryProcessServiceImpl(WayDao wayDao, DeliveryDao deliveryDao, BillDao billDao) {
        this.wayDao = wayDao;
        this.deliveryDao = deliveryDao;
        this.billDao = billDao;
    }

    @Override
    public PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws AskedDataIsNotExist {
        Mapper<DeliveryCostAndTimeDto, PriceAndTimeOnDeliveryDto> mapper = deliveryCostAndTime -> PriceAndTimeOnDeliveryDto.builder()
                .costInCents(deliveryCostAndTime.getCostInCents())
                .timeOnWayInHours(deliveryCostAndTime.getTimeOnWayInHours())
                .build();

        return mapper.map(wayDao.findByLocalitySand_IdAndLocalityGet_Id(deliveryInfoRequestDto.getLocalitySandID()
                , deliveryInfoRequestDto.getLocalityGetID(), deliveryInfoRequestDto.getDeliveryWeight()).orElseThrow(AskedDataIsNotExist::new));
    }

    @Override
    public boolean initializeDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException {
        long price;
        try {
            price = wayDao.getPrise(deliveryOrderCreateDto.getLocalitySandID()
                    , deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight());
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            throw new UnsupportableWeightFactorException();
        }
        long newDeliveryId;
        try {
            newDeliveryId = deliveryDao.createDelivery(deliveryOrderCreateDto.getAddresseeEmail(), initiatorId, deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight());
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            throw new FailCreateDeliveryException();
        }
        return billDao.createBill(price, newDeliveryId, initiatorId);
    }

    @Override
    public List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId) {
        List<DeliveryInfoToGetDto> toReturn = new ArrayList<>();
        Mapper<Delivery, DeliveryInfoToGetDto> mapper = (delivery -> DeliveryInfoToGetDto.builder()
                .addresserEmail(delivery.getAddresser().getEmail())
                .deliveryId(delivery.getId())
                .localitySandName(delivery.getWay().getLocalitySand().getNameEn())
                .localityGetName(delivery.getWay().getLocalityGet().getNameEn())
                .build());
        for (Delivery d : deliveryDao.getDeliveryInfoToGet(userId)) {
            toReturn.add(mapper.map(d));
        }
        return toReturn;
    }

    @Override
    public void ConfirmGettingDelivery(long userId, long deliveryId) {
        deliveryDao.confirmGettingDelivery(userId, deliveryId);
    }

}