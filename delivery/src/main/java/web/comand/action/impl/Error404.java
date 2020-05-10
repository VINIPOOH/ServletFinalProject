package web.comand.action.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.ERROR_404_FILE_NAME;
import static web.constants.PageConstance.MAIN_WEB_FOLDER;


public class Error404 implements ActionCommand {
    private static Logger log = LogManager.getLogger(Error404.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug(request.getMethod()+" Error404");
        return MAIN_WEB_FOLDER + ERROR_404_FILE_NAME;
    }
}
