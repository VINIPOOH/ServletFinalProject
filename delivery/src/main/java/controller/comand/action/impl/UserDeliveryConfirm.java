package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;
import dal.entity.User;
import bll.service.BillService;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.AttributeConstants.SESSION_USER;
import static controller.constants.PageConstance.*;

public class UserDeliveryConfirm extends MultipleMethodCommand {

    private final BillService billService;

    public UserDeliveryConfirm(BillService billService) {
        this.billService = billService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        request.setAttribute("BillInfoToPay",billService.getInfoToPayBillsByUserID(((User)request.getSession().getAttribute(SESSION_USER)).getId()));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        long o =Long.parseLong(request.getParameter("Id"));
        billService.payForDelivery(((User)request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter("Id")));
        request.setAttribute("BillInfoToPay",billService.getInfoToPayBillsByUserID(((User)request.getSession().getAttribute(SESSION_USER)).getId()));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME;
    }
}
