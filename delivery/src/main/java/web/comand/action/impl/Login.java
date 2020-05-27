package web.comand.action.impl;

import bl.exeption.NoSuchUserException;
import bl.service.UserService;
import dal.entity.User;
import dto.LoginInfoDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.Validator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static web.constant.AttributeConstants.LOGGINED_USER_NAMES;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.*;

public class Login extends MultipleMethodCommand {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static Logger log = LogManager.getLogger(Login.class);
    private final Validator loginDtoValidator;
    private final UserService userService;
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";

    public Login(Validator loginDtoValidator, UserService userService) {
        this.loginDtoValidator = loginDtoValidator;
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");
        return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        boolean isValid = loginDtoValidator.isValid(request);
        log.debug("isValidRequest = " + isValid);
        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME;
        }
        LoginInfoDto loginInfoDto = getLoginInfoDtoRequestDtoMapper(request).mapToDto(request);
        Map<String, HttpSession> loggedUsers = (Map<String, HttpSession>) request
                .getSession().getServletContext()
                .getAttribute(LOGGINED_USER_NAMES);
        if (loggedUsers.containsKey(loginInfoDto.getUsername())) {
            loggedUsers.get(loginInfoDto.getUsername()).invalidate();
        }
        try {
            User user = userService.loginUser(loginInfoDto);
            loggedUsers.put(loginInfoDto.getUsername(), request.getSession());
            request.getSession().setAttribute(SESSION_USER, user);
            return REDIRECT_COMMAND + USER_PROFILE_REQUEST_COMMAND;
        } catch (NoSuchUserException ignored) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME;
        }
    }

    private RequestDtoMapper<LoginInfoDto> getLoginInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> LoginInfoDto.builder()
                .username(request.getParameter(USERNAME))
                .password(request.getParameter(PASSWORD))
                .build();
    }

}
