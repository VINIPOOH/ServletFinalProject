package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;
import dto.RegistrationInfoDto;
import dto.maper.RequestDtoMapper;
import dto.validation.Validator;
import exeptions.OccupiedLoginException;
import service.UserService;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.ExceptionInfoForJspConstants.INPUT_HAS_ERRORS;
import static controller.constants.ExceptionInfoForJspConstants.INPUT_LOGIN_ALREADY_TAKEN;
import static controller.constants.PageConstance.REDIRECT_ON_LOGIN;
import static controller.constants.PageConstance.REGISTRATION_PATH;

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
        return REGISTRATION_PATH;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        RegistrationInfoDto registrationInfoDto = registrationDtoMapper.mapToDto(request);
        if (!registrationInfoDtoValidator.isValid(registrationInfoDto)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return REGISTRATION_PATH;
        }
        return processingServiseRegistrationRequest(request, registrationInfoDto);
    }

    private String processingServiseRegistrationRequest(HttpServletRequest request, RegistrationInfoDto registrationInfoDto) {
        try {
            userService.addNewUserToDB(registrationInfoDto);
            return REDIRECT_ON_LOGIN;
        } catch (OccupiedLoginException e) {
            request.setAttribute(INPUT_LOGIN_ALREADY_TAKEN, true);
            return REGISTRATION_PATH;
        }
    }
}
