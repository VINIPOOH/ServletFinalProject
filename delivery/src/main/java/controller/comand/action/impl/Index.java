package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.PageConstance.INDEX_PATH;

public class Index extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {
        return INDEX_PATH;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
