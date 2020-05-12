package web.comand.action.impl;

import bl.service.DeliveryProcessService;
import dal.entity.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;
import web.dto.validation.IDValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constants.AttributeConstants.SESSION_LANG;
import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.PageConstance.*;

public class UserDeliveryGet extends MultipleMethodCommand {
    private static final String DELIVERIES_WHICH_ADDRESSED_FOR_USER = "deliveriesWhichAddressedForUser";
    private static final String DELIVERY_ID = "deliveryId";
    private static Logger log = LogManager.getLogger(UserDeliveryGet.class);

    private final IDValidator idValidator;
    private final DeliveryProcessService deliveryProcessService;

    public UserDeliveryGet(IDValidator idValidator, DeliveryProcessService deliveryProcessService) {
        this.idValidator = idValidator;
        this.deliveryProcessService = deliveryProcessService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");

        request.setAttribute(DELIVERIES_WHICH_ADDRESSED_FOR_USER, deliveryProcessService.getInfoToGetDeliverisByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        if (!idValidator.isValid(request, DELIVERY_ID)) {
            log.error("id is not valid client is broken");

            throw new RuntimeException();
        }
        deliveryProcessService.confirmGettingDelivery(((User) request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter(DELIVERY_ID)));
        request.setAttribute(DELIVERIES_WHICH_ADDRESSED_FOR_USER, deliveryProcessService.getInfoToGetDeliverisByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }
}
