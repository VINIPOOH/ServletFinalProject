package web.comand.action.impl;

import web.comand.action.ActionCommand;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.INDEX_FILE_NAME;
import static web.constants.PageConstance.MAIN_WEB_FOLDER;


public class Index implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        return MAIN_WEB_FOLDER + INDEX_FILE_NAME;
    }
}
