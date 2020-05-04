package web.comand.action.impl;

import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.LOGIN_REQUEST_COMMAND;
import static web.constants.PageConstance.REDIRECT_COMMAND;

public class LogOut implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return REDIRECT_COMMAND + LOGIN_REQUEST_COMMAND;
    }
}
