package web.comand.factory;

import bl.service.*;
import web.comand.action.ActionCommand;
import web.comand.action.impl.*;
import web.dto.validation.*;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {


    private static final String LOGIN_KEY = "anonymous/login";
    private static final String LOGOUT_KEY = "user/logout";
    private static final String REGISTRATION_KEY = "anonymous/registration";
    private static final String ADMIN_KEY = "admin";
    private static final String USER_USERPROFILE_KEY = "user/userprofile";
    private static final String INDEX_KEY = "anonymous/index";
    private static final String KEY_404 = "404";
    private static final String COUNTER_KEY = "counter";
    private static final String USER_USER_DELIVERY_INITIATION_KEY = "user/user-delivery-initiation";
    private static final String USER_USER_DELIVERY_REQUEST_CONFIRM_KEY = "user/user-delivery-request-confirm";
    private static final String USER_DELIVERS_TO_GET_KEY = "user/delivers-to-get";
    private static final String USER_USER_STATISTIC_KEY = "user/user-statistic";
    private static final Validator LOGIN_INFO_DTO_VALIDATOR = new LoginDtoValidator();
    private static final Validator REGISTRATION_INFO_DTO_VALIDATOR = new RegistrationDtoValidator();
    private static final Validator DELIVERY_ORDER_CREATE_DTO_VALIDATOR = new DeliveryOrderCreateDtoValidator();
    private static final IDValidator ID_VALIDATOR = new IDValidatorImpl();
    private static final UserService USER_SERVICE = ServicesContext.getObject(UserService.class);
    private static final LocalityService LOCALITY_SERVICE = ServicesContext.getObject(LocalityService.class);
    private static final DeliveryProcessService DELIVERY_PROCESS_SERVICE = ServicesContext.getObject(DeliveryProcessService.class);
    private static final BillService BILL_SERVICE = ServicesContext.getObject(BillService.class);
    private static final ActionCommand LOGIN = new Login(LOGIN_INFO_DTO_VALIDATOR, USER_SERVICE);
    private static final ActionCommand LOGOUT = new LogOut();
    private static final ActionCommand REGISTRATION = new Registration(REGISTRATION_INFO_DTO_VALIDATOR, USER_SERVICE);
    private static final ActionCommand ADMIN = new Admin();
    private static final ActionCommand USER_PROFILE = new UserProfile(USER_SERVICE);
    private static final ActionCommand INDEX = new Index();
    private static final ActionCommand ERROR_404 = new Error404();
    private static final ActionCommand EMPTY_COMMAND = new EmptyCommand();
    private static final ActionCommand COUNTER = new Counter(LOCALITY_SERVICE, DELIVERY_PROCESS_SERVICE);
    private static final ActionCommand USER_DELIVERY_INITIATION = new UserDeliveryInitiation(LOCALITY_SERVICE, BILL_SERVICE, DELIVERY_ORDER_CREATE_DTO_VALIDATOR);
    private static final ActionCommand USER_DELIVERY_CONFIRM = new UserDeliveryPay(BILL_SERVICE, USER_SERVICE, ID_VALIDATOR);
    private static final ActionCommand USER_DELIVERY_GET = new UserDeliveryGet(ID_VALIDATOR, DELIVERY_PROCESS_SERVICE);
    private static final ActionCommand USER_STATISTIC = new UserStatistic(BILL_SERVICE);
    private static final Map<String, ActionCommand> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put(LOGIN_KEY, LOGIN);
        COMMANDS.put(LOGOUT_KEY, LOGOUT);
        COMMANDS.put(REGISTRATION_KEY, REGISTRATION);
        COMMANDS.put(ADMIN_KEY, ADMIN);
        COMMANDS.put(USER_USERPROFILE_KEY, USER_PROFILE);
        COMMANDS.put(INDEX_KEY, INDEX);
        COMMANDS.put(KEY_404, ERROR_404);
        COMMANDS.put(COUNTER_KEY, COUNTER);
        COMMANDS.put(USER_USER_DELIVERY_INITIATION_KEY, USER_DELIVERY_INITIATION);
        COMMANDS.put(USER_USER_DELIVERY_REQUEST_CONFIRM_KEY, USER_DELIVERY_CONFIRM);
        COMMANDS.put(USER_DELIVERS_TO_GET_KEY, USER_DELIVERY_GET);
        COMMANDS.put(USER_USER_STATISTIC_KEY, USER_STATISTIC);
    }

    private CommandContext() {
    }

    public static ActionCommand defineCommand(String path) {
        return COMMANDS.getOrDefault(path, EMPTY_COMMAND);
    }
}
