package web.comand;

import javax.servlet.http.HttpServletRequest;


public interface MultipleMethodCommand {

    String doGet(HttpServletRequest request);

    String doPost(HttpServletRequest request);

}
