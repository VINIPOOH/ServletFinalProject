package web.comand.action.impl;

import bl.service.UserService;
import bl.exeptions.OccupiedLoginException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;
import web.dto.RegistrationInfoDto;
import web.dto.maper.RequestDtoMapper;
import web.dto.validation.Validator;

import javax.servlet.http.HttpServletRequest;

import static web.constants.PageConstance.*;

public class Registration extends MultipleMethodCommand {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_REPEAT = "passwordRepeat";
    private static Logger log = LogManager.getLogger(Registration.class);

    String INPUT_HAS_ERRORS = "inputHasErrors";
    String INPUT_LOGIN_ALREADY_TAKEN = "inputLoginAlreadyTaken";

    private final Validator registrationInfoDtoValidator;
    private final UserService userService;

    public Registration(Validator registrationInfoDtoValidator, UserService userService) {
        this.registrationInfoDtoValidator = registrationInfoDtoValidator;
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + REGISTRATION_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        log.debug("isValidRequest = " + registrationInfoDtoValidator.isValid(request));

        if (!registrationInfoDtoValidator.isValid(request)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + REGISTRATION_FILE_NAME;
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
            return MAIN_WEB_FOLDER + REGISTRATION_FILE_NAME;
        }
    }
}
