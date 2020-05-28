package web.comand.action.impl;

import bl.exeption.OccupiedLoginException;
import bl.service.UserService;
import dto.RegistrationInfoDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.RegistrationDtoValidator;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.*;

@Singleton
@Endpoint("anonymous/registration")
public class Registration extends MultipleMethodCommand {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_REPEAT = "passwordRepeat";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";
    private static final String INPUT_LOGIN_ALREADY_TAKEN = "inputLoginAlreadyTaken";
    private static Logger log = LogManager.getLogger(Registration.class);
    @InjectByType
    private RegistrationDtoValidator registrationInfoDtoValidator;
    @InjectByType
    private UserService userService;

    public Registration() {
    }

    public Registration(RegistrationDtoValidator registrationInfoDtoValidator, UserService userService) {
        this.registrationInfoDtoValidator = registrationInfoDtoValidator;
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        boolean isValid = registrationInfoDtoValidator.isValid(request);
        log.debug("isValidRequest = " + isValid);

        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME;
        }
        return processingServiseRegistrationRequest(request, getRegistrationInfoDtoRequestDtoMapper(request).mapToDto(request));
    }

    private RequestDtoMapper<RegistrationInfoDto> getRegistrationInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return req -> RegistrationInfoDto.builder()
                .username(request.getParameter(USERNAME))
                .password(request.getParameter(PASSWORD))
                .passwordRepeat(request.getParameter(PASSWORD_REPEAT))
                .build();
    }

    private String processingServiseRegistrationRequest(HttpServletRequest request, RegistrationInfoDto registrationInfoDto) {
        try {
            userService.addNewUserToDB(registrationInfoDto);
            return REDIRECT_COMMAND + LOGIN_REQUEST_COMMAND;
        } catch (OccupiedLoginException e) {
            request.setAttribute(INPUT_LOGIN_ALREADY_TAKEN, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME;
        }
    }
}
