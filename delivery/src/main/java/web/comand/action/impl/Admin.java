package web.comand.action.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.Servlet;
import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

public class Admin implements ActionCommand {
    private static Logger log = LogManager.getLogger(Admin.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug(request.getMethod()+" admin");
        return "WEB-INF/admin/a.jsp";
    }
}
