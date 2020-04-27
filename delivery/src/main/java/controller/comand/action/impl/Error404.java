package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.PageConstance.ERROR_404;

public class Error404 extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {
        return ERROR_404;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
