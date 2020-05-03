package web.comand.action.impl;

import web.comand.action.MultipleMethodCommand;
import dal.entity.User;
import bll.service.impl.BillService;

import javax.servlet.http.HttpServletRequest;

import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.PageConstance.*;


public class UserStatistic extends MultipleMethodCommand {
    private final BillService billService;

    public UserStatistic(BillService billService) {
        this.billService = billService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        request.setAttribute("billsList", billService.getBillHistoryByUserId(((User)request.getSession().getAttribute(SESSION_USER)).getId()));

        return MAIN_WEB_FOLDER+USER_FOLDER+USER_STATISTIC_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }


}
