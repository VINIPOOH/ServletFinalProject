package dto.validation;


import dto.LoginInfoDto;

public class LoginDtoValidator implements Validator<LoginInfoDto> {

    private static final String LOGIN_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";//"^([A-Za-z \\\\d-_.]+)(@[A-Za-z]+)(\\\\.[A-Za-z]{2,4})$";

    @Override
    public boolean isValid(LoginInfoDto dto) {
        return isStringValid(dto.getUsername(), LOGIN_REGEX);
    }


}
