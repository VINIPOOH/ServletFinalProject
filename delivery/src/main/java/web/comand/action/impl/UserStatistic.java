package web.comand.action.impl;

import bl.service.BillService;
import dal.entity.User;
import dto.validation.IDValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.ActionCommand;
import web.util.Pagination;

import javax.servlet.http.HttpServletRequest;

import static web.constants.AttributeConstants.SESSION_USER;
import static web.constants.PageConstance.*;


public class UserStatistic implements ActionCommand {

    public static final String USER_USER_STATISTIC = "user/user-statistic";
    public static final int PAGE_ATRIBUTE = 1;
    public static final int PAGE_SIZE = 10;
    private static final String BILLS_LIST = "billsList";
    private static Logger log = LogManager.getLogger(UserStatistic.class);
    private final Pagination pagination;
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
        int pageAtribute = PAGE_ATRIBUTE;
        int pageSize = PAGE_SIZE;
        if (pagination.Validate(request)) {
            pageAtribute = Integer.parseInt(request.getParameter("page"));
            pageSize = Integer.parseInt(request.getParameter("size"));
        }

        pagination.paginate(pageAtribute, pageSize, billService.countAllBillsBiUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId()), request, USER_USER_STATISTIC);
        //todo rework offset
        request.setAttribute(BILLS_LIST, billService.getBillHistoryByUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (pageAtribute - 1) * pageSize, pageSize));
        return MAIN_WEB_FOLDER + USER_FOLDER + USER_STATISTIC_FILE_NAME;
    }
}
