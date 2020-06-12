package web.comand.impl;

public interface PageConstance {
    String REDIRECT_COMMAND = "redirect:";
    String MAIN_WEB_FOLDER = "/WEB-INF/";
    String USER_FOLDER = "user/";
    String ANONYMOUS_FOLDER = "anonymous/";
    String ADMIN_FOLDER = "admin/";

    String USERS_JSP = "users.infrastructure.jsp";
    String REGISTRATION_FILE_NAME = "registration.infrastructure.jsp";
    String LOGIN_FILE_NAME = "login.infrastructure.jsp";
    String INDEX_FILE_NAME = "index.infrastructure.jsp";
    String ERROR_404_FILE_NAME = "404.infrastructure.jsp";
    String COUNTER_FILE_NAME = "counter.infrastructure.jsp";
    String USER_PROFILE_FILE_NAME = "userprofile.infrastructure.jsp";
    String USER_DELIVERY_INITIATION_FILE_NAME = "user-delivery-initiation.infrastructure.jsp";
    String USER_DELIVERY_CONFIRM_DELIVERY_FILE_NAME = "user-delivery-request-confirm.infrastructure.jsp";
    String USER_DELIVERY_GET_CONFIRM_FILE_NAME = "user-deliverys-to-get.infrastructure.jsp";
    String USER_STATISTIC_FILE_NAME = "user-statistic.infrastructure.jsp";

    String BASE_REQUEST_COMMAND = "delivery/";
    String INDEX_REQUEST_COMMAND = "index";
    String LOGIN_REQUEST_COMMAND = "login";
    String ERROR_404_REQUEST_COMMAND = "404";
    String USER_PROFILE_REQUEST_COMMAND = "user/userprofile";
    String COUNTER_REQUEST_COMMAND = "counter";
    String USER_DELIVERY_INITIATION_REQUEST_COMMAND = "user-delivery-initiation";

    String REDIRECT_ON_ERROR_404_STRAIGHT = "/delivery/404";


}
