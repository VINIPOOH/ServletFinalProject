package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;
import bll.dto.DeliveryOrderCreateDto;
import bll.dto.maper.RequestDtoMapper;
import bll.dto.validation.Validator;
import dal.entity.User;
import exeptions.FailCreateDeliveryException;
import exeptions.UnsupportableWeightFactorException;
import bll.service.DeliveryProcessService;
import bll.service.LocalityService;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static controller.constants.AttributeConstants.SESSION_LANG;
import static controller.constants.AttributeConstants.SESSION_USER;
import static controller.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static controller.constants.PageConstance.*;

public class UserDeliveryInitiation extends MultipleMethodCommand {

    private final LocalityService localityService;
    private final DeliveryProcessService deliveryProcessService;
    private final RequestDtoMapper<DeliveryOrderCreateDto> deliveryOrderCreateDtoRequestDtoMapper;
    private final Validator<DeliveryOrderCreateDto> deliveryOrderCreateDtoValidator;

    public UserDeliveryInitiation(LocalityService localityService, DeliveryProcessService deliveryProcessService, RequestDtoMapper<DeliveryOrderCreateDto> deliveryOrderCreateDtoRequestDtoMapper, Validator<DeliveryOrderCreateDto> deliveryOrderCreateDtoValidator) {
        this.localityService = localityService;
        this.deliveryProcessService = deliveryProcessService;
        this.deliveryOrderCreateDtoRequestDtoMapper = deliveryOrderCreateDtoRequestDtoMapper;
        this.deliveryOrderCreateDtoValidator = deliveryOrderCreateDtoValidator;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        Locale o = (Locale) request.getSession().getAttribute(SESSION_LANG);
        String str = ((Locale) request.getSession().getAttribute(SESSION_LANG)).getLanguage();
        request.setAttribute("localityList", localityService.getLocaliseLocalities(o));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        request.setAttribute("localityList", localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        DeliveryOrderCreateDto deliveryOrderCreateDto;
        try {
            deliveryOrderCreateDto = deliveryOrderCreateDtoRequestDtoMapper.mapToDto(request);
        } catch (NumberFormatException ex) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
        }

        if (!deliveryOrderCreateDtoValidator.isValid(deliveryOrderCreateDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
        }

        try {
            deliveryProcessService.initializeDelivery(deliveryOrderCreateDto, ((User) request.getSession().getAttribute(SESSION_USER)).getId());
        } catch (UnsupportableWeightFactorException e) {
            e.printStackTrace();
        } catch (FailCreateDeliveryException e) {
            e.printStackTrace();
        }
//        Optional<PriceAndTimeOnDeliveryDto> deliveryCostAndTimeDto = deliveryProcessService.getDeliveryCostAndTimeDto(deliveryOrderCreateDto);
//        if (deliveryCostAndTimeDto.isPresent()) {
//            request.setAttribute("CostAndTimeDto", deliveryCostAndTimeDto.get());
//            return MAIN_WEB_FOLDER + USER_FOLDER +USER_DELIVERY_INITIATION_FILE_NAME;
//        }
//        request.setAttribute("IsNotExistSuchWayOrWeightForThisWay", true);
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
    }
}
