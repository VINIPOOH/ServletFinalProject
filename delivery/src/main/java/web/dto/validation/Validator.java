package web.dto.validation;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface Validator {
    boolean isValid(HttpServletRequest request);

    default boolean isStringValid(String param, String regex) {
        return param.matches(regex);
    }

    default boolean numberParamValid(long param, long minValue, long maxValue) {
        return param > minValue || param < maxValue;
    }
}
