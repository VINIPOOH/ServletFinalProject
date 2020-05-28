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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import static web.constant.AttributeConstants.LOGGINED_USER_NAMES;

public class Servlet extends HttpServlet {

    private static Logger log = LogManager.getLogger(Servlet.class);
    private static final Map<Class, Object> paramMap = new HashMap<>();
    private static final JavaConfig config = new JavaConfig("", new HashMap<>());
    private static final ApplicationContext context = new ApplicationContext(config, paramMap);
    private static final ObjectFactory objectFactory = new ObjectFactory(context);

    @Override
    public void init() throws ServletException {
        super.init();
        this.getServletContext().setAttribute(LOGGINED_USER_NAMES, new ConcurrentHashMap<String, HttpSession>());
        context.setFactory(objectFactory);
        paramMap.put(ResourceBundle.class, ResourceBundle.getBundle("db-request"));
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
        String page = context.getCommand(path).execute(request);
        if (page.contains("redirect:")) {
            response.sendRedirect(page.replace("redirect:", "/delivery/"));
        } else {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}