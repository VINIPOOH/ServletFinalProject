package controller.comand.action;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.PageConstance.ERROR_404;

public abstract class MultipleMethodCommand implements ActionCommand {

    private final String COMMAND_TYPE_GET = "GET";
    private final String COMMAND_TYPE_POST = "POST";

    @Override
    public String execute(HttpServletRequest request) {
        switch (request.getMethod()) {
            case COMMAND_TYPE_GET:
                return performGet(request);
            case COMMAND_TYPE_POST:
                return performPost(request);
            default:
                return ERROR_404;
        }
    }

    protected abstract String performGet(HttpServletRequest request);

    protected abstract String performPost(HttpServletRequest request);

}
