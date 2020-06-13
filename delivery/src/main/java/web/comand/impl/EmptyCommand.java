package web.comand.impl;

import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.*;

@Singleton
public class EmptyCommand implements MultipleMethodCommand {
    private static Logger log = LogManager.getLogger(EmptyCommand.class);

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        return REDIRECT_COMMAND + ANONYMOUS_FOLDER + INDEX_REQUEST_COMMAND;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
