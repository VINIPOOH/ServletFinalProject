package web;

import web.comand.factory.CommandSingleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet extends HttpServlet {
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

        String path = request.getRequestURI().replaceFirst(".*/delivery/", "");
        String page = CommandSingleton.defineCommand(path).execute(request);
        if (page.contains("redirect:")) {
            response.sendRedirect(page.replace("redirect:", "/delivery/"));
        } else {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}