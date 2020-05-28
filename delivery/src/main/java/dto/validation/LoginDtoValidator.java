package dto.validation;


import infrastructure.anotation.Singleton;

import javax.servlet.http.HttpServletRequest;

@Singleton
public class LoginDtoValidator {

    private static final String LOGIN_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";

    public boolean isValid(HttpServletRequest request) {
        return isStringValid(request.getParameter("username"), LOGIN_REGEX);
    }

    private boolean isStringValid(String param, String regex) {
        return param.matches(regex);
    }

}
