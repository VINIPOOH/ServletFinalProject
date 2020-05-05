package web.comand.action.impl;

import bll.service.UserService;
import dal.entity.User;
import exeptions.NoSuchUserException;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static web.constants.PageConstance.*;

public class UserProfile extends MultipleMethodCommand {

    private final UserService userService;

    public UserProfile(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        long money;
        try {
            money = Long.parseLong(request.getParameter("money"));
        }catch (NumberFormatException ex){
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME;
        }
        if (money <= 0) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME;
        }
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        try {
            userService.replenishAccountBalance(user.getId(), money);
        } catch (NoSuchUserException e) {
            throw new RuntimeException();
        }
        user.setUserMoneyInCents(user.getUserMoneyInCents() + money);
        request.setAttribute(SESSION_USER, user);
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME;
    }
}
