package web.comand.impl;

import dal.entity.User;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.ActionCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static web.constant.AttributeConstants.LOGGINED_USER_NAMES;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.LOGIN_REQUEST_COMMAND;
import static web.constant.PageConstance.REDIRECT_COMMAND;

@Singleton
@Endpoint("user/logout")
public class LogOut implements ActionCommand {
    private static Logger log = LogManager.getLogger(LogOut.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("");

        Map<String, HttpSession> loggedUsers = (Map<String, HttpSession>) request
                .getSession().getServletContext()
                .getAttribute(LOGGINED_USER_NAMES);
        loggedUsers.remove(((User) request.getSession().getAttribute(SESSION_USER)).getEmail());
        request.getSession().invalidate();
        return REDIRECT_COMMAND + LOGIN_REQUEST_COMMAND;
    }
}
