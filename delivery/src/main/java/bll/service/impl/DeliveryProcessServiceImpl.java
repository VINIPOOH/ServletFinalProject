package bll.service.impl;


import bll.dto.DeliveryInfoToGetDto;
import bll.dto.PriceAndTimeOnDeliveryDto;
import bll.dto.mapper.Mapper;
import bll.exeptions.AskedDataIsNotExist;
import dal.dao.DeliveryDao;
import dal.dao.WayDao;
import dal.dto.DeliveryCostAndTimeDto;
import dal.entity.Delivery;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.dto.DeliveryInfoRequestDto;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DeliveryProcessServiceImpl implements bll.service.DeliveryProcessService {
    private static Logger log = LogManager.getLogger(DeliveryProcessServiceImpl.class);

    private final WayDao wayDao;
    private final DeliveryDao deliveryDao;

    public DeliveryProcessServiceImpl(WayDao wayDao, DeliveryDao deliveryDao) {
        log.debug("created");

        this.wayDao = wayDao;
        this.deliveryDao = deliveryDao;
    }

    @Override
    public PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws AskedDataIsNotExist {
        log.debug("deliveryInfoRequestDto - "+deliveryInfoRequestDto);

        Mapper<DeliveryCostAndTimeDto, PriceAndTimeOnDeliveryDto> mapper =
                getDeliveryCostAndTimeDtoPriceAndTimeOnDeliveryDtoMapper();
        return mapper.map(wayDao.findByLocalitySandIdAndLocalityGetId(deliveryInfoRequestDto.getLocalitySandID(),
                deliveryInfoRequestDto.getLocalityGetID(),
                deliveryInfoRequestDto.getDeliveryWeight()).orElseThrow(AskedDataIsNotExist::new));
    }


    @Override
    public List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId, Locale locale) {
        log.debug("userId - "+userId+" localeLang - "+locale.getLanguage() );

        return deliveryDao.getDeliveryInfoToGet(userId, locale).stream()
                .map(getDeliveryInfoToGetDtoMapper(locale)::map)
                .collect(Collectors.toList());
    }

    @Override
    public void confirmGettingDelivery(long userId, long deliveryId) {
        log.debug("userId -"+userId+" deliveryId -"+deliveryId);

        deliveryDao.confirmGettingDelivery(userId, deliveryId);
    }

    private Mapper<DeliveryCostAndTimeDto, PriceAndTimeOnDeliveryDto> getDeliveryCostAndTimeDtoPriceAndTimeOnDeliveryDtoMapper() {
        return deliveryCostAndTime -> PriceAndTimeOnDeliveryDto.builder()
                .costInCents(deliveryCostAndTime.getCostInCents())
                .timeOnWayInHours(deliveryCostAndTime.getTimeOnWayInHours())
                .build();
    }

    private Mapper<Delivery, DeliveryInfoToGetDto> getDeliveryInfoToGetDtoMapper(Locale locale) {
        return delivery -> {
            DeliveryInfoToGetDto deliveryInfo = DeliveryInfoToGetDto.builder()
                    .addresserEmail(delivery.getBill().getUser().getEmail())
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

}
