package web.comand.impl;

import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodController;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.ERROR_404_FILE_NAME;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;

/**
 * Process request for which application context has no mapping
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
public class PhantomController implements MultipleMethodController {
    private static final Logger log = LogManager.getLogger(PhantomController.class);

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
