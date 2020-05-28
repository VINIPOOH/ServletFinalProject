package web.comand.impl;

import dal.entity.User;
import dto.validation.Validator;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.Singleton;
import logiclayer.exeption.NoSuchUserException;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.*;

@Singleton
@Endpoint("user/userprofile")
public class UserProfile extends MultipleMethodCommand {
    private static final String MONEY = "money";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";
    private static Logger log = LogManager.getLogger(UserProfile.class);
    @InjectByType
    private UserService userService;

    public UserProfile() {
    }

    public UserProfile(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        boolean isValid = getValidator().isValid(request);
        log.debug("isValidRequest = " + isValid);

        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME;
        }
        long money = Long.parseLong(request.getParameter(MONEY));
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

    private Validator getValidator() {
        return request -> {
            try {
                return Long.parseLong(request.getParameter(MONEY)) > 0;
            } catch (NumberFormatException ex) {
                return false;
            }
        };
    }
}
