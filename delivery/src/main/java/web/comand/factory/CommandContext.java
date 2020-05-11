package web.comand.factory;

import bl.service.*;
import web.comand.action.ActionCommand;
import web.comand.action.impl.*;
import web.dto.validation.*;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {


    private static final Validator LOGIN_INFO_DTO_VALIDATOR = new LoginDtoValidator();
    private static final Validator REGISTRATION_INFO_DTO_VALIDATOR = new RegistrationDtoValidator();
    private static final Validator DELIVERY_ORDER_CREATE_DTO_VALIDATOR = new DeliveryOrderCreateDtoValidator();

    private static final IDValidator ID_VALIDATOR = new IDValidatorImpl();

    private static final UserService USER_SERVICE = ServicesContext.getUserService();
    private static final LocalityService LOCALITY_SERVICE = ServicesContext.getLocalityService();
    private static final DeliveryProcessService DELIVERY_PROCESS_SERVICE = ServicesContext.getDeliveryProcessService();
    private static final BillService BILL_SERVICE = ServicesContext.getBillService();

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
    private static final ActionCommand USER_DELIVERY_CONFIRM = new UserDeliveryPay(BILL_SERVICE, ID_VALIDATOR);
    private static final ActionCommand USER_DELIVERY_GET = new UserDeliveryGet(ID_VALIDATOR, DELIVERY_PROCESS_SERVICE);
    private static final ActionCommand USER_STATISTIC = new UserStatistic(BILL_SERVICE);

    private static final Map<String, ActionCommand> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("login", LOGIN);
        COMMANDS.put("logout", LOGOUT);
        COMMANDS.put("registration", REGISTRATION);
        COMMANDS.put("admin", ADMIN);
        COMMANDS.put("user/userprofile", USER_PROFILE);
        COMMANDS.put("index", INDEX);
        COMMANDS.put("404", ERROR_404);
        COMMANDS.put("counter", COUNTER);
        COMMANDS.put("user/user-delivery-initiation", USER_DELIVERY_INITIATION);
        COMMANDS.put("user/user-delivery-request-confirm", USER_DELIVERY_CONFIRM);
        COMMANDS.put("user/delivers-to-get", USER_DELIVERY_GET);
        COMMANDS.put("user/user-statistic", USER_STATISTIC);
    }

    private CommandContext() {
    }

    public static ActionCommand defineCommand(String path) {
        return COMMANDS.getOrDefault(path, EMPTY_COMMAND);
    }
}
