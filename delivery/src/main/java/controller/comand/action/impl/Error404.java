package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.PageConstance.*;


public class Error404 extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {
        return MAIN_WEB_FOLDER+ ERROR_404_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
