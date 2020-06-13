package web.comand.impl;

import infrastructure.anotation.Endpoint;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.*;

@Singleton
@Endpoint("anonymous/index")
public class Index implements MultipleMethodCommand {
    private static final Logger log = LogManager.getLogger(Index.class);

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + INDEX_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
