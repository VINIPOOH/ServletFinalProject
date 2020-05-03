package web.comand.action.impl;

import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.ERROR_404_FILE_NAME;
import static web.constants.PageConstance.MAIN_WEB_FOLDER;


public class Error404 implements ActionCommand {


    @Override
    public String execute(HttpServletRequest request) {
        return MAIN_WEB_FOLDER + ERROR_404_FILE_NAME;
    }
}
