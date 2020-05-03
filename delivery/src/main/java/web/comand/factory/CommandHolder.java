package web.comand.factory;

import bll.service.*;
import web.comand.action.ActionCommand;
import web.comand.action.impl.*;
import web.dto.DeliveryInfoRequestDto;
import web.dto.DeliveryOrderCreateDto;
import web.dto.LoginInfoDto;
import web.dto.RegistrationInfoDto;
import web.dto.maper.*;
import web.dto.validation.*;

import java.util.HashMap;
import java.util.Map;

public class CommandHolder {
    private static final String DB_PROPERTY_FILE = "database";
    private static final String DB_REQUEST_FILE = "db-request";


    private static final Validator<LoginInfoDto> LOGIN_INFO_DTO_VALIDATOR = new LoginDtoValidator();
    private static final Validator<RegistrationInfoDto> REGISTRATION_INFO_DTO_VALIDATOR = new RegistrationDtoValidator();
    private static final Validator<DeliveryInfoRequestDto> DELIVERY_INFO_REQUEST_DTO_VALIDATOR = new DeliveryInfoRequestDtoValidator();
    private static final Validator<DeliveryOrderCreateDto> DELIVERY_ORDER_CREATE_DTO_VALIDATOR = new DeliveryOrderCreateDtoValidator();

    private static final RequestDtoMapper<LoginInfoDto> LOGIN_INFO_DTO_REQUEST_DTO_MAPPER = new LoginRequestDtoMapper();
    private static final RequestDtoMapper<RegistrationInfoDto> REGISTRATION_INFO_DTO_REQUEST_DTO_MAPPER = new RegistrationRequestDtoMapper();
    private static final RequestDtoMapper<DeliveryInfoRequestDto> DELIVERY_INFO_REQUEST_DTO_REQUEST_DTO_MAPPER = new DeliveryInfoRequestToDtoMapper();
    private static final RequestDtoMapper<DeliveryOrderCreateDto> DELIVERY_ORDER_CREATE_DTO_REQUEST_DTO_MAPPER = new DeliveryOrderCreateDtoMapper();

    private static final UserService USER_SERVICE = ServicesSingleton.getUserService();
    private static final LocalityService LOCALITY_SERVICE = ServicesSingleton.getLocalityService();
    private static final DeliveryProcessService DELIVERY_PROCESS_SERVICE = ServicesSingleton.getDeliveryProcessService();
    private static final BillService BILL_SERVICE = ServicesSingleton.getBillService();

    private static final ActionCommand LOGIN = new Login(LOGIN_INFO_DTO_VALIDATOR, LOGIN_INFO_DTO_REQUEST_DTO_MAPPER, USER_SERVICE);
    private static final ActionCommand LOGOUT = new LogOut();
    private static final ActionCommand REGISTRATION = new Registration(REGISTRATION_INFO_DTO_REQUEST_DTO_MAPPER, REGISTRATION_INFO_DTO_VALIDATOR, USER_SERVICE);
    private static final ActionCommand ADMIN = new Admin();
    private static final ActionCommand USER_PROFILE = new UserProfile(USER_SERVICE);
    private static final ActionCommand INDEX = new Index();
    private static final ActionCommand ERROR_404 = new Error404();
    private static final ActionCommand EMPTY_COMMAND = new EmptyCommand();
    private static final ActionCommand COUNTER = new Counter(LOCALITY_SERVICE, DELIVERY_PROCESS_SERVICE, DELIVERY_INFO_REQUEST_DTO_REQUEST_DTO_MAPPER, DELIVERY_INFO_REQUEST_DTO_VALIDATOR);
    private static final ActionCommand USER_DELIVERY_INITIATION = new UserDeliveryInitiation(LOCALITY_SERVICE,DELIVERY_PROCESS_SERVICE, DELIVERY_ORDER_CREATE_DTO_REQUEST_DTO_MAPPER, DELIVERY_ORDER_CREATE_DTO_VALIDATOR );
    private static final ActionCommand USER_DELIVERY_CONFIRM = new UserDeliveryConfirm(BILL_SERVICE);
    private static final ActionCommand USER_DELIVERY_GET = new UserDeliveryGet(DELIVERY_PROCESS_SERVICE);
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
        COMMANDS.put("user/user-delivery-initiation",USER_DELIVERY_INITIATION);
        COMMANDS.put("user/user-delivery-request-confirm",USER_DELIVERY_CONFIRM);
        COMMANDS.put("user/delivers-to-get", USER_DELIVERY_GET);
        COMMANDS.put("user/user-statistic", USER_STATISTIC);
    }

    private CommandHolder() {
    }

    public static ActionCommand defineCommand(String path) {
        return COMMANDS.getOrDefault(path, EMPTY_COMMAND);
    }
}
