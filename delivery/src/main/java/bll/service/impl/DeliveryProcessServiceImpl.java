package bll.service.impl;


import bll.dto.DeliveryInfoToGetDto;
import bll.dto.PriceAndTimeOnDeliveryDto;
import bll.dto.mapper.Mapper;
import dal.dao.DeliveryDao;
import dal.dao.WayDao;
import dal.dto.DeliveryCostAndTimeDto;
import dal.entity.Delivery;
import exeptions.AskedDataIsNotExist;
import web.dto.DeliveryInfoRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeliveryProcessServiceImpl implements bll.service.DeliveryProcessService {

    private final WayDao wayDao;
    private final DeliveryDao deliveryDao;

    public DeliveryProcessServiceImpl(WayDao wayDao, DeliveryDao deliveryDao) {
        this.wayDao = wayDao;
        this.deliveryDao = deliveryDao;
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
    public List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId, Locale locale) {
        List<DeliveryInfoToGetDto> toReturn = new ArrayList<>();
        Mapper<Delivery, DeliveryInfoToGetDto> mapper = (delivery ->{
                DeliveryInfoToGetDto deliveryInfo = DeliveryInfoToGetDto.builder()
                .addresserEmail(delivery.getAddresser().getEmail())
                .deliveryId(delivery.getId())
                .localitySandName(delivery.getWay().getLocalitySand().getNameEn())
                .localityGetName(delivery.getWay().getLocalityGet().getNameEn())
                .build();
            if(locale.getLanguage().equals("ru")){
                deliveryInfo.setLocalitySandName(delivery.getWay().getLocalitySand().getNameRu());
                deliveryInfo.setLocalityGetName(delivery.getWay().getLocalityGet().getNameRu());
            }else {
                deliveryInfo.setLocalitySandName(delivery.getWay().getLocalitySand().getNameEn());
                deliveryInfo.setLocalityGetName(delivery.getWay().getLocalityGet().getNameEn());
            }
            return deliveryInfo;
        });


        for (Delivery d : deliveryDao.getDeliveryInfoToGet(userId, locale)) {
            toReturn.add(mapper.map(d));
        }
        return toReturn;
    }

    @Override
    public void ConfirmGettingDelivery(long userId, long deliveryId) {
        deliveryDao.confirmGettingDelivery(userId, deliveryId);
    }

}
