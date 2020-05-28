package web.comand.action.impl;

import bl.service.UserService;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.*;

@Singleton
@Endpoint("admin/users")
public class Admin implements ActionCommand {
    public static final String USERS_LIST = "usersList";
    private static Logger log = LogManager.getLogger(Admin.class);

    @InjectByType
    private UserService userService;

    public Admin() {
    }

    public Admin(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        log.debug(request.getMethod() + " admin");

        request.setAttribute(USERS_LIST, userService.getAllUsers());
        return MAIN_WEB_FOLDER + ADMIN_FOLDER + USERS_JSP;
    }
}
