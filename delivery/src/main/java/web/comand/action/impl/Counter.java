package web.comand.action.impl;

import bll.dto.PriceAndTimeOnDeliveryDto;
import bll.service.DeliveryProcessService;
import bll.service.LocalityService;
import exeptions.AskedDataIsNotExist;
import web.comand.action.MultipleMethodCommand;
import web.dto.DeliveryInfoRequestDto;
import web.dto.maper.RequestDtoMapper;
import web.dto.validation.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constants.AttributeConstants.SESSION_LANG;
import static web.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static web.constants.PageConstance.COUNTER_FILE_NAME;
import static web.constants.PageConstance.MAIN_WEB_FOLDER;

public class Counter extends MultipleMethodCommand {

    private final LocalityService localityService;
    private final DeliveryProcessService deliveryProcessService;

    public Counter(LocalityService localityService, DeliveryProcessService deliveryProcessService) {
        this.localityService = localityService;
        this.deliveryProcessService = deliveryProcessService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        Locale o = (Locale) request.getSession().getAttribute(SESSION_LANG);
        request.setAttribute("localityList", localityService.getLocaliseLocalities(o));
        return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        request.setAttribute("localityList", localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        DeliveryInfoRequestDto deliveryInfoRequestDto;
        try {
            deliveryInfoRequestDto = getDeliveryInfoRequestDtoRequestDtoMapper(request).mapToDto(request);
        } catch (NumberFormatException ex) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        }

        if (!getDeliveryInfoRequestDtoValidator().isValid(deliveryInfoRequestDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        }
        PriceAndTimeOnDeliveryDto deliveryCostAndTimeDto = null;
        try {
            deliveryCostAndTimeDto = deliveryProcessService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto);
            request.setAttribute("CostAndTimeDto", deliveryCostAndTimeDto);
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            request.setAttribute("IsNotExistSuchWayOrWeightForThisWay", true);
        }
        return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
    }

    private RequestDtoMapper<DeliveryInfoRequestDto> getDeliveryInfoRequestDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> DeliveryInfoRequestDto.builder()
                .deliveryWeight(Integer.parseInt(request.getParameter("deliveryWeight")))
                .localityGetID(Long.parseLong(request.getParameter("localityGetID")))
                .localitySandID(Long.parseLong(request.getParameter("localitySandID")))
                .build();
    }

    private Validator<DeliveryInfoRequestDto> getDeliveryInfoRequestDtoValidator() {
        return dto -> (dto.getDeliveryWeight() > 0) && (dto.getLocalityGetID() > 0) && (dto.getLocalitySandID() > 0);
    }
}
