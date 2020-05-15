package web.comand.action.impl;

import bl.service.BillService;
import dal.entity.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.ActionCommand;
import web.dto.validation.IDValidator;
import web.util.Pagination;

import javax.servlet.http.HttpServletRequest;

import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.PageConstance.*;


public class UserStatistic implements ActionCommand {

    private static final String BILLS_LIST = "billsList";
    public static final String USER_USER_STATISTIC = "user/user-statistic";
    private final Pagination pagination;
    private static Logger log = LogManager.getLogger(UserStatistic.class);

    private final IDValidator idValidator;
    private final BillService billService;

    public UserStatistic(Pagination pagination, IDValidator idValidator, BillService billService) {
        this.pagination = pagination;
        this.idValidator = idValidator;
        this.billService = billService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("");

        if (!pagination.Validate(request)) {
            return REDIRECT_COMMAND + ANONYMOUS_FOLDER + ERROR_404_FILE_NAME;
        }
        int pageAtribute = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("size"));

        pagination.paginate(pageAtribute, pageSize, billService.countAllBillsBiUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId()), request, USER_USER_STATISTIC);
        request.setAttribute(BILLS_LIST, billService.getBillHistoryByUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (pageAtribute - 1) * pageSize, pageSize));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_STATISTIC_FILE_NAME;
    }
}
