package web.comand.action.impl;

import bl.service.BillService;
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

public class UserDeliveryPay extends MultipleMethodCommand {
    public static final String BILL_INFO_TO_PAY = "BillInfoToPay";
    public static final String ID = "Id";
    public static final String ID1 = "Id";
    private static Logger log = LogManager.getLogger(UserDeliveryPay.class);

    private final BillService billService;
    private final IDValidator idValidator;

    public UserDeliveryPay(BillService billService, IDValidator idValidator) {
        this.billService = billService;
        this.idValidator = idValidator;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");

        request.setAttribute(BILL_INFO_TO_PAY, billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        log.debug("");
        if (!idValidator.isValid(request, ID)){
            log.error("id is not valid client is broken");
            throw new RuntimeException();
        }
        billService.payForDelivery(((User) request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter(ID1)));
        request.setAttribute(BILL_INFO_TO_PAY, billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME;
    }
}
