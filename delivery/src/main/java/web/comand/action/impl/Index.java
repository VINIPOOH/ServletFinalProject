package web.comand.action.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.INDEX_FILE_NAME;
import static web.constants.PageConstance.MAIN_WEB_FOLDER;


public class Index implements ActionCommand {
    private static Logger log = LogManager.getLogger(Index.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + INDEX_FILE_NAME;
    }
}
