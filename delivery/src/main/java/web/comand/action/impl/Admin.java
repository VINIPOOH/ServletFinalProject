package web.comand.action.impl;

import web.comand.action.ActionCommand;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

public class Admin implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        return "WEB-INF/admin/a.jsp";
    }
}
