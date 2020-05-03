package web.comand.action.impl;

import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.INDEX_FILE_NAME;
import static web.constants.PageConstance.MAIN_WEB_FOLDER;


public class Index extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {

        return MAIN_WEB_FOLDER + INDEX_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
