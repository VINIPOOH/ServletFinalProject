package web.comand.action.impl;

import bll.service.UserService;
import web.comand.action.MultipleMethodCommand;
import web.dto.RegistrationInfoDto;
import web.dto.maper.RequestDtoMapper;
import web.dto.validation.Validator;
import exeptions.OccupiedLoginException;

import javax.servlet.http.HttpServletRequest;

import static web.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static web.constants.ExceptionInfoForJspConstants.INPUT_LOGIN_ALREADY_TAKEN;
import static web.constants.PageConstance.*;

public class Registration extends MultipleMethodCommand {

    private final RequestDtoMapper<RegistrationInfoDto> registrationDtoMapper;
    private final Validator<RegistrationInfoDto> registrationInfoDtoValidator;
    private final UserService userService;

    public Registration(RequestDtoMapper<RegistrationInfoDto> registrationDtoMapper, Validator<RegistrationInfoDto> registrationInfoDtoValidator, UserService userService) {
        this.registrationDtoMapper = registrationDtoMapper;
        this.registrationInfoDtoValidator = registrationInfoDtoValidator;
        this.userService = userService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        return MAIN_WEB_FOLDER+REGISTRATION_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        RegistrationInfoDto registrationInfoDto = registrationDtoMapper.mapToDto(request);
        if (!registrationInfoDtoValidator.isValid(registrationInfoDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER+REGISTRATION_FILE_NAME;
        }
        return processingServiseRegistrationRequest(request, registrationInfoDto);
    }

    private String processingServiseRegistrationRequest(HttpServletRequest request, RegistrationInfoDto registrationInfoDto) {
        try {
            userService.addNewUserToDB(registrationInfoDto);
            return REDIRECT_COMMAND+LOGIN_REQUEST_COMMAND;
        } catch (OccupiedLoginException e) {
            request.setAttribute(INPUT_LOGIN_ALREADY_TAKEN, true);
            return MAIN_WEB_FOLDER+REGISTRATION_FILE_NAME;
        }
    }
}
