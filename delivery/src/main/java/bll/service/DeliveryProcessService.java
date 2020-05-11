package bll.service;

import bll.dto.DeliveryInfoToGetDto;
import bll.dto.PriceAndTimeOnDeliveryDto;
import web.dto.DeliveryInfoRequestDto;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface DeliveryProcessService {
    Optional<PriceAndTimeOnDeliveryDto> getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto);

    List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId, Locale locale);

    void confirmGettingDelivery(long userId, long deliveryId);
}
