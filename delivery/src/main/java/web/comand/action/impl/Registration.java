package web.comand.action.impl;

import bll.service.UserService;
import dal.exeptions.OccupiedLoginException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;
import web.dto.RegistrationInfoDto;
import web.dto.maper.RequestDtoMapper;
import web.dto.validation.Validator;

import javax.servlet.http.HttpServletRequest;

import static web.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static web.constants.ExceptionInfoForJspConstants.INPUT_LOGIN_ALREADY_TAKEN;
import static web.constants.PageConstance.*;

public class Registration extends MultipleMethodCommand {
    private static Logger log = LogManager.getLogger(Registration.class);

    private final Validator<RegistrationInfoDto> registrationInfoDtoValidator;
    private final UserService userService;

    public Registration(Validator<RegistrationInfoDto> registrationInfoDtoValidator, UserService userService) {
        this.registrationInfoDtoValidator = registrationInfoDtoValidator;
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug(request.getMethod()+" Registration");
        return MAIN_WEB_FOLDER + REGISTRATION_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        log.debug(request.getMethod()+" Registration");

        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDtoRequestDtoMapper(request).mapToDto(request);
        if (!registrationInfoDtoValidator.isValid(registrationInfoDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + REGISTRATION_FILE_NAME;
        }
        return processingServiseRegistrationRequest(request, registrationInfoDto);
    }

    private RequestDtoMapper<RegistrationInfoDto> getRegistrationInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return req -> RegistrationInfoDto.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .passwordRepeat(request.getParameter("passwordRepeat"))
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
