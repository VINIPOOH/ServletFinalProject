package dto.validation;

import javax.servlet.http.HttpServletRequest;

public interface IDValidator {
    boolean isValid(HttpServletRequest request, String... idFieldNames);
}
