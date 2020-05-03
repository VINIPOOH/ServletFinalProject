package bll.service;

import bll.dto.DeliveryInfoToGetDto;
import bll.dto.PriceAndTimeOnDeliveryDto;
import bll.exeptions.UnsupportableWeightFactorException;
import exeptions.AskedDataIsNotExist;
import exeptions.FailCreateDeliveryException;
import web.dto.DeliveryInfoRequestDto;
import web.dto.DeliveryOrderCreateDto;

import java.util.List;

public interface DeliveryProcessService {
    PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws AskedDataIsNotExist;

    List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId);

    void ConfirmGettingDelivery(long userId, long deliveryId);
}
