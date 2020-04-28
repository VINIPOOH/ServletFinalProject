package controller.constants;

public interface PageConstance {
    String REDIRECT_ON_HOME = "redirect:index";
    String REDIRECT_ON_LOGIN = "redirect:login";
    String REDIRECT_ON_ERROR_404 = "redirect:404";
    String REDIRECT_ON_USER = "redirect:user";
    String REDIRECT_ON_COUNTER = "redirect:counter";

    String REDIRECT_ON_LOGIN_STRAIGHT = "/delivery/login";
    String REDIRECT_ON_ERROR_404_STRAIGHT = "/delivery/404";

    String REGISTRATION_PATH = "/WEB-INF/registration.jsp";
    String LOGIN_PATH = "/WEB-INF/login.jsp";
    String INDEX_PATH = "/WEB-INF/index.jsp";
    String ERROR_404 = "/WEB-INF/404.jsp";
    String COUNTER_PATH = "/WEB-INF/counter.jsp";
}
