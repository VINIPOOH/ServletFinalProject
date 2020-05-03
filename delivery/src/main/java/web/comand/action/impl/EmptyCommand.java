package web.comand.action.impl;

import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.INDEX_REQUEST_COMMAND;
import static web.constants.PageConstance.REDIRECT_COMMAND;


public class EmptyCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        return REDIRECT_COMMAND + INDEX_REQUEST_COMMAND;
    }
}
