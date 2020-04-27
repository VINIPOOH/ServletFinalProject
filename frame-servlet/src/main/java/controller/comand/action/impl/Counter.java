package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

public class Counter extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {
        return "WEB-INF/counter.jsp";
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
