package web.comand.action.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.*;


public class EmptyCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(EmptyCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("");

        return REDIRECT_COMMAND + ANONYMOUS_FOLDER + INDEX_REQUEST_COMMAND;
    }
}
