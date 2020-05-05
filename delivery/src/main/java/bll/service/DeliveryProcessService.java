package bll.service;

import bll.dto.DeliveryInfoToGetDto;
import bll.dto.PriceAndTimeOnDeliveryDto;
import bll.exeptions.AskedDataIsNotExist;
import web.dto.DeliveryInfoRequestDto;

import java.util.List;
import java.util.Locale;

public interface DeliveryProcessService {
    PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws AskedDataIsNotExist;

    List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId, Locale locale);

    void confirmGettingDelivery(long userId, long deliveryId);
}
