package dto.validation;


import dto.RegistrationInfoDto;

public class RegistrationDtoValidator implements Validator<RegistrationInfoDto> {

    private static final String LOGIN_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";//"^([A-Za-z \\\\d-_.]+)(@[A-Za-z]+)(\\\\.[A-Za-z]{2,4})$";

    @Override
    public boolean isValid(RegistrationInfoDto dto) {
        return isStringValid(dto.getUsername(), LOGIN_REGEX)
                && dto.getPassword().equals(dto.getPasswordRepeat());
    }


}
