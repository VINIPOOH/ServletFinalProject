package web.comand.action.impl;

import bll.service.DeliveryProcessService;
import bll.service.LocalityService;
import bll.exeptions.AskedDataIsNotExist;
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
        request.setAttribute("localityList", localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        request.setAttribute("localityList", localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        if (!getDeliveryInfoRequestDtoValidator().isValid(request)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        }
        try {
            request.setAttribute("CostAndTimeDto", deliveryProcessService.getDeliveryCostAndTimeDto
                    (getDeliveryInfoRequestDtoRequestDtoMapper(request).mapToDto(request)));
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

    private Validator<HttpServletRequest> getDeliveryInfoRequestDtoValidator() {
        return request -> {
            try {
                return ((Integer.parseInt(request.getParameter("deliveryWeight")) > 0) &&
                        (Long.parseLong(request.getParameter("localityGetID")) > 0) &&
                        (Long.parseLong(request.getParameter("localitySandID")) > 0));
            } catch (NumberFormatException ex) {
                return false;
            }
        };
    }
}
