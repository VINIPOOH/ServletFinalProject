package web.comand.action.impl;

import bll.service.DeliveryProcessService;
import dal.entity.User;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constants.AttributeConstants.SESSION_LANG;
import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.PageConstance.*;

public class UserDeliveryGet extends MultipleMethodCommand {

    private final DeliveryProcessService deliveryProcessService;

    public UserDeliveryGet(DeliveryProcessService deliveryProcessService) {
        this.deliveryProcessService = deliveryProcessService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        request.setAttribute("deliveriesWhichAddressedForUser", deliveryProcessService.getInfoToGetDeliverisByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        deliveryProcessService.confirmGettingDelivery(((User) request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter("deliveryId")));
        request.setAttribute("deliveriesWhichAddressedForUser", deliveryProcessService.getInfoToGetDeliverisByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }
}
