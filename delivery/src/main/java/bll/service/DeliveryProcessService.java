package bll.service;

import bll.dto.DeliveryInfoToGetDto;
import bll.dto.PriceAndTimeOnDeliveryDto;
import exeptions.AskedDataIsNotExist;
import exeptions.FailCreateDeliveryException;
import exeptions.UnsupportableWeightFactorException;
import web.dto.DeliveryInfoRequestDto;
import web.dto.DeliveryOrderCreateDto;

import java.util.List;

public interface DeliveryProcessService {
    PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws AskedDataIsNotExist;

    boolean initializeDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException;

    List<DeliveryInfoToGetDto> getInfoToGetDeliverisByUserID(long userId);

    void ConfirmGetingDelivery(long userId, long deliveryId);
}
