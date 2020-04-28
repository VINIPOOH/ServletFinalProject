package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.PageConstance.INDEX_FILE_NAME;
import static controller.constants.PageConstance.MAIN_WEB_FOLDER;


public class Index extends MultipleMethodCommand {
    @Override
    protected String performGet(HttpServletRequest request) {

        return MAIN_WEB_FOLDER+INDEX_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
