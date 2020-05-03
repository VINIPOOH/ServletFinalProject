package web.comand.action.impl;

import web.comand.action.MultipleMethodCommand;
import web.dto.LoginInfoDto;
import web.dto.maper.RequestDtoMapper;
import web.dto.validation.Validator;
import exeptions.NoSuchUserException;
import bll.service.impl.UserService;

import javax.servlet.http.HttpServletRequest;

import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.ExceptionInfoForJspConstants.INCORRECT_LOGIN_OR_PASSWORD;
import static web.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static web.constants.PageConstance.*;

public class Login extends MultipleMethodCommand {

    private final Validator<LoginInfoDto> loginDtoValidator;
    private final RequestDtoMapper<LoginInfoDto> loginInfoDtoRequestDtoMapper;
    private final UserService userService;

    public Login(Validator<LoginInfoDto> loginDtoValidator, RequestDtoMapper<LoginInfoDto> loginInfoDtoRequestDtoMapper, UserService userService) {
        this.loginDtoValidator = loginDtoValidator;
        this.loginInfoDtoRequestDtoMapper = loginInfoDtoRequestDtoMapper;
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        return MAIN_WEB_FOLDER+LOGIN_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {

        LoginInfoDto loginInfoDto = loginInfoDtoRequestDtoMapper.mapToDto(request);
        if (!loginDtoValidator.isValid(loginInfoDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER+LOGIN_FILE_NAME;
        }
        return processingServiceLoginRequest(request, loginInfoDto);
    }

    private String processingServiceLoginRequest(HttpServletRequest request, LoginInfoDto loginInfoDto) {
        try {
            request.getSession().setAttribute(SESSION_USER, userService.loginUser(loginInfoDto));
            return REDIRECT_COMMAND+USER_PROFILE_REQUEST_COMMAND;
        } catch (NoSuchUserException ignored) {
            request.setAttribute(INCORRECT_LOGIN_OR_PASSWORD, true);
            return MAIN_WEB_FOLDER+LOGIN_FILE_NAME;
        }
    }
}
