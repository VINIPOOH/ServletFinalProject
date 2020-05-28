package web.comand;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.ERROR_404_FILE_NAME;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;


public abstract class MultipleMethodCommand implements ActionCommand {

    private static final String COMMAND_TYPE_GET = "GET";
    private static final String COMMAND_TYPE_POST = "POST";

    @Override
    public String execute(HttpServletRequest request) {
        switch (request.getMethod()) {
            case COMMAND_TYPE_GET:
                return performGet(request);
            case COMMAND_TYPE_POST:
                return performPost(request);
            default:
                return MAIN_WEB_FOLDER + ERROR_404_FILE_NAME;
        }
    }

    protected abstract String performGet(HttpServletRequest request);

    protected abstract String performPost(HttpServletRequest request);

}
