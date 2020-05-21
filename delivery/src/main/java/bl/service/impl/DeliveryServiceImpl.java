package bl.service.impl;


import dal.dao.DeliveryDao;
import dal.dao.WayDao;
import dal.dto.DeliveryCostAndTimeDto;
import dal.entity.Delivery;
import dto.DeliveryInfoRequestDto;
import dto.DeliveryInfoToGetDto;
import dto.PriceAndTimeOnDeliveryDto;
import dto.mapper.Mapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static bl.service.ServicesConstants.RUSSIAN_LANG_COD;

public class DeliveryServiceImpl implements bl.service.DeliveryProcessService {
    private static Logger log = LogManager.getLogger(DeliveryServiceImpl.class);

    private final WayDao wayDao;
    private final DeliveryDao deliveryDao;

    public DeliveryServiceImpl(WayDao wayDao, DeliveryDao deliveryDao) {
        log.debug("created");

        this.wayDao = wayDao;
        this.deliveryDao = deliveryDao;
    }

    @Override
    public Optional<PriceAndTimeOnDeliveryDto> getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) {
        log.debug("deliveryInfoRequestDto - " + deliveryInfoRequestDto);
        return wayDao.findByLocalitySandIdAndLocalityGetId(deliveryInfoRequestDto.getLocalitySandID(),
                deliveryInfoRequestDto.getLocalityGetID(), deliveryInfoRequestDto.getDeliveryWeight())
                .map(deliveryCostAndTimeDto -> getDeliveryCostAndTimeDtoPriceAndTimeOnDeliveryDtoMapper().map(deliveryCostAndTimeDto));
    }


    @Override
    public List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId, Locale locale) {
        log.debug("userId - " + userId + " localeLang - " + locale.getLanguage());

        return deliveryDao.getDeliveryInfoToGet(userId, locale).stream()
                .map(getDeliveryInfoToGetDtoMapper(locale)::map)
                .collect(Collectors.toList());
    }

    @Override
    public void confirmGettingDelivery(long userId, long deliveryId) {
        log.debug("userId -" + userId + " deliveryId -" + deliveryId);

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
            if (locale.getLanguage().equals(RUSSIAN_LANG_COD)) {
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
