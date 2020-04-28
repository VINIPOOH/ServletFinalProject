package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;
import dto.DeliveryCostAndTimeDto;
import dto.DeliveryInfoRequestDto;
import dto.maper.DeliveryInfoRequestToDtoMapper;
import dto.maper.RequestDtoMapper;
import dto.validation.DeliveryInfoRequestDtoValidator;
import dto.validation.Validator;
import service.DeliveryProcessService;
import service.LocalityService;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static controller.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static controller.constants.PageConstance.COUNTER_PATH;
import static controller.constants.PageConstance.REDIRECT_ON_COUNTER;

public class Counter extends MultipleMethodCommand {

    private final LocalityService localityService;
    private final DeliveryProcessService deliveryProcessService;
    private final RequestDtoMapper<DeliveryInfoRequestDto> deliveryInfoRequestToDtoMapper;
    private final Validator<DeliveryInfoRequestDto> deliveryInfoRequestDtoValidator;

    public Counter(LocalityService localityService, DeliveryProcessService deliveryProcessService, RequestDtoMapper<DeliveryInfoRequestDto> deliveryInfoRequestToDtoMapper, Validator<DeliveryInfoRequestDto> deliveryInfoRequestDtoValidator) {
        this.localityService = localityService;
        this.deliveryProcessService = deliveryProcessService;
        this.deliveryInfoRequestToDtoMapper = deliveryInfoRequestToDtoMapper;
        this.deliveryInfoRequestDtoValidator = deliveryInfoRequestDtoValidator;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        Object o =localityService.getLocaliseLocalities();
        request.setAttribute("localityList",o);
        return COUNTER_PATH;
    }

    @Override
    protected String performPost(HttpServletRequest request) {

        DeliveryInfoRequestDto deliveryInfoRequestDto = deliveryInfoRequestToDtoMapper.mapToDto(request);
        if (!deliveryInfoRequestDtoValidator.isValid(deliveryInfoRequestDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return REDIRECT_ON_COUNTER;
        }
        Optional<DeliveryCostAndTimeDto> deliveryCostAndTimeDto =deliveryProcessService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto);
        if(deliveryCostAndTimeDto.isPresent()){
            request.setAttribute("CostAndTimeDto", deliveryCostAndTimeDto.get());

            return REDIRECT_ON_COUNTER;
        }
        request.setAttribute("IsNotExistSuchWayOrWeightForThisWay", true);
        return REDIRECT_ON_COUNTER;
    }
}
