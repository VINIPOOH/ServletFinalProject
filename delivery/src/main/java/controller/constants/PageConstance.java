package controller.constants;

public interface PageConstance {
    String REDIRECT_COMMAND = "redirect:";
    String MAIN_WEB_FOLDER = "/WEB-INF/";
    String USER_FOLDER = "user/";
    String ADMIN_FOLDER = "admin/";

    String REGISTRATION_FILE_NAME = "registration.jsp";
    String LOGIN_FILE_NAME = "login.jsp";
    String INDEX_FILE_NAME = "index.jsp";
    String ERROR_404_FILE_NAME = "404.jsp";
    String COUNTER_FILE_NAME = "counter.jsp";
    String USER_PROFILE_FILE_NAME = "userprofile.jsp";
    String USER_DELIVERY_INITIATION_FILE_NAME = "user-delivery-initiation.jsp";
    String USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME = "user-delivery-request-confirm.jsp";

    String BASE_REQUEST_COMMAND = "delivery/";
    String INDEX_REQUEST_COMMAND = "index";
    String LOGIN_REQUEST_COMMAND = "login";
    String ERROR_404_REQUEST_COMMAND = "404";
    String USER_PROFILE_REQUEST_COMMAND = "user/userprofile";
    String COUNTER_REQUEST_COMMAND = "counter";
    String USER_DELIVERY_INITIATION_REQUEST_COMMAND ="user-delivery-initiation";

    String REDIRECT_ON_ERROR_404_STRAIGHT = "/delivery/404";


}
