package web.comand.action.impl;

import bl.exeptions.FailCreateDeliveryException;
import bl.exeptions.UnsupportableWeightFactorException;
import bl.service.BillService;
import bl.service.LocalityService;
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
import static web.constants.PageConstance.*;

public class UserDeliveryInitiation extends MultipleMethodCommand {
    private static final String LOCALITY_LIST = "localityList";
    private static final String DELIVERY_WEIGHT = "deliveryWeight";
    private static final String LOCALITY_GET_ID = "localityGetID";
    private static final String LOCALITY_SAND_ID = "localitySandID";
    private static final String ADDRESSEE_EMAIL = "addresseeEmail";
    private static Logger log = LogManager.getLogger(UserDeliveryInitiation.class);
    private final LocalityService localityService;
    private final BillService billService;
    private final Validator deliveryOrderCreateDtoValidator;
    private String INPUT_HAS_ERRORS = "inputHasErrors";
    private String UNSUPPORTABLE_WEIGHT = "unsupportableWeightOrWay";

    public UserDeliveryInitiation(LocalityService localityService, BillService billService, Validator deliveryOrderCreateDtoValidator) {
        this.localityService = localityService;
        this.billService = billService;
        this.deliveryOrderCreateDtoValidator = deliveryOrderCreateDtoValidator;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug(request.getMethod() + " UserDeliveryInitiation");

        Locale o = (Locale) request.getSession().getAttribute(SESSION_LANG);
        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities(o));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        log.debug("isValidRequest = " + deliveryOrderCreateDtoValidator.isValid(request));

        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
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
                .deliveryWeight(Integer.parseInt(request.getParameter(DELIVERY_WEIGHT)))
                .localityGetID(Long.parseLong(request.getParameter(LOCALITY_GET_ID)))
                .localitySandID(Long.parseLong(request.getParameter(LOCALITY_SAND_ID)))
                .addresseeEmail(request.getParameter(ADDRESSEE_EMAIL))
                .build();
    }


}
