package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;
import bll.dto.DeliveryCostAndTimeDto;
import bll.dto.DeliveryInfoRequestDto;
import bll.dto.maper.RequestDtoMapper;
import bll.dto.validation.Validator;
import bll.service.DeliveryProcessService;
import bll.service.LocalityService;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

import static controller.constants.AttributeConstants.SESSION_LANG;
import static controller.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static controller.constants.PageConstance.COUNTER_FILE_NAME;
import static controller.constants.PageConstance.MAIN_WEB_FOLDER;

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
        Locale o = (Locale) request.getSession().getAttribute(SESSION_LANG);
        String str = ((Locale) request.getSession().getAttribute(SESSION_LANG)).getLanguage();
        request.setAttribute("localityList", localityService.getLocaliseLocalities(o));
        return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        request.setAttribute("localityList", localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        DeliveryInfoRequestDto deliveryInfoRequestDto;
        try {
            deliveryInfoRequestDto = deliveryInfoRequestToDtoMapper.mapToDto(request);
        }catch (NumberFormatException ex){
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        }

        if (!deliveryInfoRequestDtoValidator.isValid(deliveryInfoRequestDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        }
        Optional<DeliveryCostAndTimeDto> deliveryCostAndTimeDto = deliveryProcessService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto);
        if (deliveryCostAndTimeDto.isPresent()) {
            request.setAttribute("CostAndTimeDto", deliveryCostAndTimeDto.get());
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        }
        request.setAttribute("IsNotExistSuchWayOrWeightForThisWay", true);
        return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
    }
}
