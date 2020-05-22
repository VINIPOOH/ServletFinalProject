package web.comand.action.impl;

import bl.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.ADMIN_FOLDER;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;

public class Admin implements ActionCommand {
    public static final String USERS_LIST = "usersList";
    private static Logger log = LogManager.getLogger(Admin.class);

    private final UserService userService;

    public Admin(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        log.debug(request.getMethod() + " admin");

        request.setAttribute(USERS_LIST, userService.getAllUsers());
        return MAIN_WEB_FOLDER + ADMIN_FOLDER + "users.jsp";
    }
}
