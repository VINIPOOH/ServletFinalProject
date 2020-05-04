package bll.service;

import bll.dto.DeliveryInfoToGetDto;
import bll.dto.PriceAndTimeOnDeliveryDto;
import exeptions.AskedDataIsNotExist;
import web.dto.DeliveryInfoRequestDto;

import java.util.List;

public interface DeliveryProcessService {
    PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws AskedDataIsNotExist;

    List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId);

    void ConfirmGettingDelivery(long userId, long deliveryId);
}
