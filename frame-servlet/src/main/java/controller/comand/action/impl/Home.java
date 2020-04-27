package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.PageConstance.HOME_PATH;
import static controller.constants.PageConstance.INDEX_PATH;

public class Home extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {
        request.setAttribute(localityService.getLocalities());
        return HOME_PATH;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
