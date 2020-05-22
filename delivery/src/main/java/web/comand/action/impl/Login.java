package web.comand.action.impl;

import bl.exeptions.NoSuchUserException;
import bl.service.UserService;
import dto.LoginInfoDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.Validator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.PageConstance.*;

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
        log.debug("isValidRequest = " + loginDtoValidator.isValid(request));
        if (!loginDtoValidator.isValid(request)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME;
        }

        return processingServiceLoginRequest(request, getLoginInfoDtoRequestDtoMapper(request).mapToDto(request));
    }

    private RequestDtoMapper<LoginInfoDto> getLoginInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> LoginInfoDto.builder()
                .username(request.getParameter(USERNAME))
                .password(request.getParameter(PASSWORD))
                .build();
    }

    private String processingServiceLoginRequest(HttpServletRequest request, LoginInfoDto loginInfoDto) {
        try {
            request.getSession().setAttribute(SESSION_USER, userService.loginUser(loginInfoDto));
            return REDIRECT_COMMAND + USER_PROFILE_REQUEST_COMMAND;
        } catch (NoSuchUserException ignored) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME;
        }
    }
}
