package web.comand.impl;

import dal.entity.User;
import dto.validation.IDValidator;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.service.BillService;
import logiclayer.service.UserService;
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
@Endpoint("user/user-delivery-request-confirm")
public class UserDeliveryPay implements MultipleMethodCommand {
    private static final String BILL_INFO_TO_PAY = "BillInfoToPay";
    private static final String ID = "Id";
    private static final String ID1 = "Id";
    private static final Logger log = LogManager.getLogger(UserDeliveryPay.class);
    private static final String NOT_ENOUGH_MONEY = "notEnoughMoney";
    @InjectByType
    private BillService billService;
    @InjectByType
    private UserService userService;
    @InjectByType
    private IDValidator idValidator;

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        request.setAttribute(BILL_INFO_TO_PAY, billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        log.debug("");
        if (!idValidator.isValid(request, ID)) {
            log.error("id is not valid client is broken");
            throw new RuntimeException();
        }
        User sessionUser = (User) request.getSession().getAttribute(SESSION_USER);
        if (billService.payForDelivery(sessionUser.getId(), Long.parseLong(request.getParameter(ID1)))) {
            sessionUser.setUserMoneyInCents(userService.getUserBalance(sessionUser.getId()));
        } else {
            request.setAttribute(NOT_ENOUGH_MONEY, true);
        }
        request.setAttribute(BILL_INFO_TO_PAY, billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME;
    }
}
