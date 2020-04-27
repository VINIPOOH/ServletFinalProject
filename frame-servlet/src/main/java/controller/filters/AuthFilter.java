package controller.filters;

import entity.RoleType;
import entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static controller.constants.AttributeConstants.SESSION_USER;
import static controller.constants.PageConstance.*;


public class AuthFilter implements Filter {

    private static final String INDEX_REQUEST = "/delivery/index";
    private static final String HOME_REQUEST = "/delivery/";
    private static final String LOGIN_REQUEST = "/login";
    private static final String REGISTRATION_REQUEST = "/registration";
    private static final String ADMIN_REQUEST = "/admin";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String path = req.getRequestURI();

        if (path.endsWith(INDEX_REQUEST)||path.endsWith(HOME_REQUEST)) {
            filterChain.doFilter(request, response);
            return;
        }
        boolean isOnlyForNotLogin = (path.contains(LOGIN_REQUEST) || path.contains(REGISTRATION_REQUEST));
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null && isOnlyForNotLogin) {
            res.sendRedirect(REDIRECT_ON_ERROR_404_STRAIGHT);
            return;
        }
        if(user==null){
            if(isOnlyForNotLogin){
                filterChain.doFilter(request, response);
                return;
            }
            request.getRequestDispatcher(ERROR_404).forward(request, response);
            return;
        }
        if ((!(user.getRoleType().equals(RoleType.ROLE_ADMIN))) && path.contains(ADMIN_REQUEST)) {
            res.sendRedirect(REDIRECT_ON_ERROR_404_STRAIGHT);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
