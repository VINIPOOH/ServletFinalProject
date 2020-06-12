package web.comand.impl;

import dal.entity.User;
import dto.validation.IDValidator;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.service.DeliveryService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constant.AttributeConstants.SESSION_LANG;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.*;

@Singleton
@NeedConfig
@Endpoint("user/delivers-to-get")
public class UserDeliveryGet extends MultipleMethodCommand {
    private static final String DELIVERIES_WHICH_ADDRESSED_FOR_USER = "deliveriesWhichAddressedForUser";
    private static final String DELIVERY_ID = "deliveryId";
    private static Logger log = LogManager.getLogger(UserDeliveryGet.class);

    @InjectByType
    private IDValidator idValidator;
    @InjectByType
    private DeliveryService deliveryService;

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");

        request.setAttribute(DELIVERIES_WHICH_ADDRESSED_FOR_USER, deliveryService.getInfoToGetDeliverisByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        boolean isValid = idValidator.isValid(request, DELIVERY_ID);
        log.debug("isValid" + isValid);
        if (!isValid) {
            log.error("id is not valid client is broken");
//todo
            throw new RuntimeException();
        }
        deliveryService.confirmGettingDelivery(((User) request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter(DELIVERY_ID)));
        request.setAttribute(DELIVERIES_WHICH_ADDRESSED_FOR_USER, deliveryService.getInfoToGetDeliverisByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }
}
