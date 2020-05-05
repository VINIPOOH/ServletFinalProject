package web.comand.action.impl;

import bll.exeptions.UnsupportableWeightFactorException;
import bll.service.BillService;
import bll.service.LocalityService;
import dal.entity.User;
import exeptions.FailCreateDeliveryException;
import web.comand.action.MultipleMethodCommand;
import web.dto.DeliveryOrderCreateDto;
import web.dto.maper.RequestDtoMapper;
import web.dto.validation.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constants.AttributeConstants.SESSION_LANG;
import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static web.constants.PageConstance.*;

public class UserDeliveryInitiation extends MultipleMethodCommand {

    private final LocalityService localityService;
    private final BillService billService;
    private final Validator<DeliveryOrderCreateDto> deliveryOrderCreateDtoValidator;

    public UserDeliveryInitiation(LocalityService localityService, BillService billService, Validator<DeliveryOrderCreateDto> deliveryOrderCreateDtoValidator) {
        this.localityService = localityService;
        this.billService = billService;
        this.deliveryOrderCreateDtoValidator = deliveryOrderCreateDtoValidator;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        Locale o = (Locale) request.getSession().getAttribute(SESSION_LANG);
        request.setAttribute("localityList", localityService.getLocaliseLocalities(o));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        request.setAttribute("localityList", localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        DeliveryOrderCreateDto deliveryOrderCreateDto;
        try {
            deliveryOrderCreateDto = getDeliveryOrderCreateDtoRequestDtoMapper(request).mapToDto(request);
        } catch (NumberFormatException ex) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
        }

        if (!deliveryOrderCreateDtoValidator.isValid(deliveryOrderCreateDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
        }

        try {
            billService.initializeBill(deliveryOrderCreateDto, ((User) request.getSession().getAttribute(SESSION_USER)).getId());
        } catch (UnsupportableWeightFactorException | FailCreateDeliveryException e) {
            //TODO
        }

        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
    }

    private RequestDtoMapper<DeliveryOrderCreateDto> getDeliveryOrderCreateDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> DeliveryOrderCreateDto.builder()
                .deliveryWeight(Integer.parseInt(request.getParameter("deliveryWeight")))
                .localityGetID(Long.parseLong(request.getParameter("localityGetID")))
                .localitySandID(Long.parseLong(request.getParameter("localitySandID")))
                .addresseeEmail(request.getParameter("addresseeEmail"))
                .build();
    }
}
