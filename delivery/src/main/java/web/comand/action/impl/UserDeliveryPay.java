package web.comand.action.impl;

import bll.service.BillService;
import dal.entity.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.constants.AttributeConstants.SESSION_LANG;
import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.PageConstance.*;

public class UserDeliveryPay extends MultipleMethodCommand {
    private static Logger log = LogManager.getLogger(UserDeliveryPay.class);

    private final BillService billService;

    public UserDeliveryPay(BillService billService) {
        this.billService = billService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");

        request.setAttribute("BillInfoToPay", billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        log.debug(request.getMethod() + " UserDeliveryPay");

        //todo add validation and rework logging
        billService.payForDelivery(((User) request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter("Id")));
        request.setAttribute("BillInfoToPay", billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME;
    }
}
