package web.comand.action.impl;

import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

public class Admin extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {
        return "WEB-INF/admin/a.jsp";
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
