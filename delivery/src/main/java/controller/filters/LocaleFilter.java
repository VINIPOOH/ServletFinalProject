package controller.filters;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Locale;

import static controller.constants.AttributeConstants.REQUEST_LANG;
import static controller.constants.AttributeConstants.SESSION_LANG;


public class LocaleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getParameter(REQUEST_LANG) != null) {
            Config.set(request.getSession(), Config.FMT_LOCALE, new Locale(request.getParameter(REQUEST_LANG)));
        }else if(request.getSession().getAttribute(SESSION_LANG)==null){
            Config.set(request.getSession(), Config.FMT_LOCALE, Locale.getDefault());

        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
