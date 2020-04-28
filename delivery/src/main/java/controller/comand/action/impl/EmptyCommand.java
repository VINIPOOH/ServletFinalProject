package controller.comand.action.impl;

import controller.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.PageConstance.INDEX_REQUEST_COMMAND;
import static controller.constants.PageConstance.REDIRECT_COMMAND;


public class EmptyCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        return REDIRECT_COMMAND+INDEX_REQUEST_COMMAND ;
    }
}
