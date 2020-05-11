package web.dto.validation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.impl.Registration;

import javax.servlet.http.HttpServletRequest;

public class IDValidatorImpl implements IDValidator {
    private static Logger log = LogManager.getLogger(IDValidatorImpl.class);

    public boolean isValid(HttpServletRequest request, String[] idFieldNames) {
        try {
            for (String str : idFieldNames) {
                if (!(Long.parseLong(request.getParameter(str)) > 0)) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException ex) {
            log.error("string where expecting long");
            return false;
        }
    }
}
