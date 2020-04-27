package controller.filters;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {

    public static final String CONTENT_TYPE = "text/html";
    public static final String ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType(CONTENT_TYPE);
        servletResponse.setCharacterEncoding(ENCODING);
        servletRequest.setCharacterEncoding(ENCODING);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
