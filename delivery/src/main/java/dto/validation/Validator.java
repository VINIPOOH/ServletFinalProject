package dto.validation;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface Validator {
    boolean isValid(HttpServletRequest request);

}
