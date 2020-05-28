package dto.validation;

import javax.servlet.http.HttpServletRequest;

public class DeliveryOrderCreateDtoValidator implements Validator {
    private static final String EMAIL_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";


    @Override
    public boolean isValid(HttpServletRequest request) {
        try {
            return ((Integer.parseInt(request.getParameter("deliveryWeight")) > 0) &&
                    (Long.parseLong(request.getParameter("localityGetID")) > 0) &&
                    (Long.parseLong(request.getParameter("localitySandID")) > 0)) &&
                    isStringValid(request.getParameter("addresseeEmail"), EMAIL_REGEX);
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isStringValid(String param, String regex) {
        return param.matches(regex);
    }

}
