package dto.validation;


import javax.servlet.http.HttpServletRequest;

public class RegistrationDtoValidator implements Validator {

    private static final String LOGIN_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";


    @Override
    public boolean isValid(HttpServletRequest request) {
        return isStringValid(request.getParameter("username"), LOGIN_REGEX)
                && request.getParameter("password").equals(request.getParameter("passwordRepeat"));
    }
}