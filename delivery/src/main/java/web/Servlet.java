package web;

import infrastructure.ApplicationContext;
import infrastructure.Config.JavaConfig;
import infrastructure.ObjectFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import static web.constant.AttributeConstants.CONTEXT;
import static web.constant.AttributeConstants.LOGGINED_USER_NAMES;

public class Servlet extends HttpServlet {

    private static Logger log = LogManager.getLogger(Servlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().setAttribute(LOGGINED_USER_NAMES, new ConcurrentHashMap<String, HttpSession>());

        Map<Class, Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put(ResourceBundle.class, ResourceBundle.getBundle("db-request"));
        ApplicationContext context = new ApplicationContext(new JavaConfig(""), paramMap, new ConcurrentHashMap<>());
        ObjectFactory objectFactory = new ObjectFactory(context);
        context.setFactory(objectFactory);
        context.init();
        getServletContext().setAttribute(CONTEXT, context);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("servlet called with request - " + request.getRequestURI());
        String path = request.getRequestURI().replaceFirst(".*/delivery/", "");
        String page = ((ApplicationContext) getServletContext().getAttribute(CONTEXT)).getCommand(path).execute(request);
        if (page.contains("redirect:")) {
            response.sendRedirect(page.replace("redirect:", "/delivery/"));
        } else {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}