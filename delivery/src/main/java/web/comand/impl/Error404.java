package web.comand.impl;

import infrastructure.anotation.Endpoint;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.ERROR_404_FILE_NAME;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;

@Singleton
@Endpoint("404")
public class Error404 implements MultipleMethodCommand {
    private static Logger log = LogManager.getLogger(Error404.class);

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + ERROR_404_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
