package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;
import dto.LoginInfoDto;
import dto.maper.RequestDtoMapper;
import dto.validation.Validator;
import exeptions.NoSuchUserException;
import service.UserService;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.AttributeConstants.SESSION_USER;
import static controller.constants.ExceptionInfoForJspConstants.INCORRECT_LOGIN_OR_PASSWORD;
import static controller.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static controller.constants.PageConstance.*;

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
        return LOGIN_PATH;
    }

    @Override
    protected String performPost(HttpServletRequest request) {

        LoginInfoDto loginInfoDto = loginInfoDtoRequestDtoMapper.mapToDto(request);
        if (!loginDtoValidator.isValid(loginInfoDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return LOGIN_PATH;
        }
        return processingServiceLoginRequest(request, loginInfoDto);
    }

    private String processingServiceLoginRequest(HttpServletRequest request, LoginInfoDto loginInfoDto) {
        try {
            request.getSession().setAttribute(SESSION_USER, userService.loginUser(loginInfoDto));
            return REDIRECT_ON_USER;
        } catch (NoSuchUserException ignored) {
            request.setAttribute(INCORRECT_LOGIN_OR_PASSWORD, true);
            return LOGIN_PATH;
        }
    }
}
