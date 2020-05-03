package web.comand.action.impl;

import bll.service.BillService;
import dal.entity.User;
import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.PageConstance.*;


public class UserStatistic implements ActionCommand {
    private final BillService billService;

    public UserStatistic(BillService billService) {
        this.billService = billService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("billsList", billService.getBillHistoryByUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId()));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_STATISTIC_FILE_NAME;
    }
}
