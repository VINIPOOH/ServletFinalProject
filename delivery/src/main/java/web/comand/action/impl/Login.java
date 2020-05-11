package web.comand.action.impl;

import bl.exeptions.NoSuchUserException;
import bl.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;
import web.dto.LoginInfoDto;
import web.dto.maper.RequestDtoMapper;
import web.dto.validation.Validator;

import javax.servlet.http.HttpServletRequest;

import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static web.constants.PageConstance.*;

public class Login extends MultipleMethodCommand {
    private static Logger log = LogManager.getLogger(Login.class);
    private final Validator<LoginInfoDto> loginDtoValidator;
    private final UserService userService;

    public Login(Validator<LoginInfoDto> loginDtoValidator, UserService userService) {
        this.loginDtoValidator = loginDtoValidator;
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");
        return MAIN_WEB_FOLDER + LOGIN_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        log.debug(request.getMethod() + " Login");

        //todo validation login and then rework log here
        LoginInfoDto loginInfoDto = getLoginInfoDtoRequestDtoMapper(request).mapToDto(request);
        if (!loginDtoValidator.isValid(loginInfoDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + LOGIN_FILE_NAME;
        }
        return processingServiceLoginRequest(request, loginInfoDto);
    }

    private RequestDtoMapper<LoginInfoDto> getLoginInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> LoginInfoDto.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .build();
    }

    private String processingServiceLoginRequest(HttpServletRequest request, LoginInfoDto loginInfoDto) {
        try {
            request.getSession().setAttribute(SESSION_USER, userService.loginUser(loginInfoDto));
            return REDIRECT_COMMAND + USER_PROFILE_REQUEST_COMMAND;
        } catch (NoSuchUserException ignored) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + LOGIN_FILE_NAME;
        }
    }
}
