package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

public class User extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {
        return "WEB-INF/user/u.jsp";
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
