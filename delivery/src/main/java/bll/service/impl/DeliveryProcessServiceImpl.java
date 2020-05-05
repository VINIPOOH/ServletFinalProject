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

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DeliveryProcessServiceImpl implements bll.service.DeliveryProcessService {

    private final WayDao wayDao;
    private final DeliveryDao deliveryDao;

    public DeliveryProcessServiceImpl(WayDao wayDao, DeliveryDao deliveryDao) {
        this.wayDao = wayDao;
        this.deliveryDao = deliveryDao;
    }

    @Override
    public PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws AskedDataIsNotExist {
        Mapper<DeliveryCostAndTimeDto, PriceAndTimeOnDeliveryDto> mapper =
                getDeliveryCostAndTimeDtoPriceAndTimeOnDeliveryDtoMapper();
        return mapper.map(wayDao.findByLocalitySandIdAndLocalityGetId(deliveryInfoRequestDto.getLocalitySandID(),
                deliveryInfoRequestDto.getLocalityGetID(),
                deliveryInfoRequestDto.getDeliveryWeight()).orElseThrow(AskedDataIsNotExist::new));
    }

    private Mapper<DeliveryCostAndTimeDto, PriceAndTimeOnDeliveryDto> getDeliveryCostAndTimeDtoPriceAndTimeOnDeliveryDtoMapper() {
        return deliveryCostAndTime -> PriceAndTimeOnDeliveryDto.builder()
                .costInCents(deliveryCostAndTime.getCostInCents())
                .timeOnWayInHours(deliveryCostAndTime.getTimeOnWayInHours())
                .build();
    }


    @Override
    public List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId, Locale locale) {
        return deliveryDao.getDeliveryInfoToGet(userId, locale).stream()
                .map(getDeliveryInfoToGetDtoMapper(locale)::map)
                .collect(Collectors.toList());
    }

    private Mapper<Delivery, DeliveryInfoToGetDto> getDeliveryInfoToGetDtoMapper(Locale locale) {
        return delivery -> {
            DeliveryInfoToGetDto deliveryInfo = DeliveryInfoToGetDto.builder()
                    .addresserEmail(delivery.getAddresser().getEmail())
                    .deliveryId(delivery.getId())
                    .localitySandName(delivery.getWay().getLocalitySand().getNameEn())
                    .localityGetName(delivery.getWay().getLocalityGet().getNameEn())
                    .build();
            if (locale.getLanguage().equals("ru")) {
                deliveryInfo.setLocalitySandName(delivery.getWay().getLocalitySand().getNameRu());
                deliveryInfo.setLocalityGetName(delivery.getWay().getLocalityGet().getNameRu());
            } else {
                deliveryInfo.setLocalitySandName(delivery.getWay().getLocalitySand().getNameEn());
                deliveryInfo.setLocalityGetName(delivery.getWay().getLocalityGet().getNameEn());
            }
            return deliveryInfo;
        };
    }

    @Override
    public void confirmGettingDelivery(long userId, long deliveryId) {
        deliveryDao.confirmGettingDelivery(userId, deliveryId);
    }

}
