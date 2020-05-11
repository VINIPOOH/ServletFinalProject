package web.comand.action.impl;

import bll.exeptions.FailCreateDeliveryException;
import bll.exeptions.UnsupportableWeightFactorException;
import bll.service.BillService;
import bll.service.LocalityService;
import dal.entity.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;
import web.dto.DeliveryOrderCreateDto;
import web.dto.maper.RequestDtoMapper;
import web.dto.validation.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constants.AttributeConstants.SESSION_LANG;
import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static web.constants.ExceptionInfoForJspConstants.UNSUPPORTABLE_WEIGHT;
import static web.constants.PageConstance.*;

public class UserDeliveryInitiation extends MultipleMethodCommand {
    private static Logger log = LogManager.getLogger(UserDeliveryInitiation.class);

    private final LocalityService localityService;
    private final BillService billService;
    private final Validator<HttpServletRequest> deliveryOrderCreateDtoValidator;

    public UserDeliveryInitiation(LocalityService localityService, BillService billService, Validator<HttpServletRequest> deliveryOrderCreateDtoValidator) {
        this.localityService = localityService;
        this.billService = billService;
        this.deliveryOrderCreateDtoValidator = deliveryOrderCreateDtoValidator;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug(request.getMethod() + " UserDeliveryInitiation");

        Locale o = (Locale) request.getSession().getAttribute(SESSION_LANG);
        request.setAttribute("localityList", localityService.getLocaliseLocalities(o));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        log.debug("isValidRequest = " + deliveryOrderCreateDtoValidator.isValid(request));

        request.setAttribute("localityList", localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        if (!deliveryOrderCreateDtoValidator.isValid(request)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
        }
        try {
            billService.initializeBill(getDeliveryOrderCreateDtoRequestDtoMapper(request).mapToDto(request), ((User) request.getSession().getAttribute(SESSION_USER)).getId());
        } catch (UnsupportableWeightFactorException | FailCreateDeliveryException e) {
            request.setAttribute(UNSUPPORTABLE_WEIGHT, true);
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
