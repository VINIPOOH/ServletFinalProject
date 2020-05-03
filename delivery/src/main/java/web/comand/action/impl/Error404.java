package web.comand.action.impl;

import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.ERROR_404_FILE_NAME;
import static web.constants.PageConstance.MAIN_WEB_FOLDER;


public class Error404 extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {
        return MAIN_WEB_FOLDER + ERROR_404_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
