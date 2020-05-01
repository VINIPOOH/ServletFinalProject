package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;
import entity.User;
import service.BillService;
import service.DeliveryProcessService;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.AttributeConstants.SESSION_USER;
import static controller.constants.PageConstance.*;

public class UserDeliveryGet extends MultipleMethodCommand {

    private final DeliveryProcessService deliveryProcessService;

    public UserDeliveryGet(DeliveryProcessService deliveryProcessService) {
        this.deliveryProcessService = deliveryProcessService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        request.setAttribute("deliveriesWhichAddressedForUser",deliveryProcessService.getInfoToGetDeliverisByUserID(((User)request.getSession().getAttribute(SESSION_USER)).getId()));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        long o =Long.parseLong(request.getParameter("deliveryId"));
        deliveryProcessService.ConfirmGetingDelivery(((User)request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter("deliveryId")));
        request.setAttribute("deliveriesWhichAddressedForUser",deliveryProcessService.getInfoToGetDeliverisByUserID(((User)request.getSession().getAttribute(SESSION_USER)).getId()));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME;
    }
}
