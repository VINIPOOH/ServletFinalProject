package controller.comand.factory;

import controller.comand.action.ActionCommand;
import controller.comand.action.impl.*;
import db.dao.BillDao;
import db.dao.DeliveryDao;
import db.dao.UserDao;
import db.dao.WayDao;
import db.dao.impl.JDBCDaoHolder;
import db.dao.impl.LocalityDao;
import dto.DeliveryInfoRequestDto;
import dto.DeliveryOrderCreateDto;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;
import dto.maper.*;
import dto.validation.*;
import service.DeliveryProcessService;
import service.LocalityService;
import service.PasswordEncoderService;
import service.UserService;

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

    private static final UserDao USER_DAO = JDBCDaoHolder.getUserDao();
    private static final LocalityDao LOCALITY_DAO = JDBCDaoHolder.getLocalityDao();
    private static final WayDao WAY_DAO = JDBCDaoHolder.getWayDao();
    private static final DeliveryDao DELIVERY_DAO = JDBCDaoHolder.getDeliveryDao();
    private static final BillDao BILL_DAO = JDBCDaoHolder.getBillDao();

    private static final PasswordEncoderService PASSWORD_ENCODER_SERVICE = new PasswordEncoderService();
    private static final UserService USER_SERVICE = new UserService(PASSWORD_ENCODER_SERVICE, USER_DAO);
    private static final LocalityService LOCALITY_SERVICE = new LocalityService(LOCALITY_DAO);
    private static final DeliveryProcessService DELIVERY_PROCESS_SERVICE = new DeliveryProcessService(WAY_DAO, DELIVERY_DAO, BILL_DAO);

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
    }

    private CommandHolder() {
    }

    public static ActionCommand defineCommand(String path) {
        return COMMANDS.getOrDefault(path, EMPTY_COMMAND);
    }
}
