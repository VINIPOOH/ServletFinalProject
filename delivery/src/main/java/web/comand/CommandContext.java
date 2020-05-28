package web.comand;

import infrastructure.ApplicationContext;
import infrastructure.Config.JavaConfig;
import infrastructure.ObjectFactory;
import web.comand.action.ActionCommand;
import web.comand.action.impl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CommandContext {

    private static final Map<Class, Object> paramMap = new HashMap<>();
    private static final JavaConfig config = new JavaConfig("", new HashMap<>());
    private static final ApplicationContext context = new ApplicationContext(config, paramMap);
    private static final ObjectFactory objectFactory = new ObjectFactory(context);
    //todo homework - init all singletons which are not lazy


    private static final String LOGIN_KEY = "anonymous/login";
    private static final String LOGOUT_KEY = "user/logout";
    private static final String REGISTRATION_KEY = "anonymous/registration";
    private static final String ADMIN_KEY = "admin/users";
    private static final String USER_USERPROFILE_KEY = "user/userprofile";
    private static final String INDEX_KEY = "anonymous/index";
    private static final String KEY_404 = "404";
    private static final String COUNTER_KEY = "counter";
    private static final String USER_USER_DELIVERY_INITIATION_KEY = "user/user-delivery-initiation";
    private static final String USER_USER_DELIVERY_REQUEST_CONFIRM_KEY = "user/user-delivery-request-confirm";
    private static final String USER_DELIVERS_TO_GET_KEY = "user/delivers-to-get";
    private static final String USER_USER_STATISTIC_KEY = "user/user-statistic";


    private static final Map<String, ActionCommand> COMMANDS = new HashMap<>();

    static {
        context.setFactory(objectFactory);
        paramMap.put(ResourceBundle.class, ResourceBundle.getBundle("db-request"));

        COMMANDS.put(LOGIN_KEY, context.getObject(Login.class));
        COMMANDS.put(LOGOUT_KEY, context.getObject(LogOut.class));
        COMMANDS.put(REGISTRATION_KEY, context.getObject(Registration.class));
        COMMANDS.put(ADMIN_KEY, context.getObject(Admin.class));
        COMMANDS.put(USER_USERPROFILE_KEY, context.getObject(UserProfile.class));
        COMMANDS.put(INDEX_KEY, context.getObject(Index.class));
        COMMANDS.put(KEY_404, context.getObject(Error404.class));
        COMMANDS.put(COUNTER_KEY, context.getObject(Counter.class));
        COMMANDS.put(USER_USER_DELIVERY_INITIATION_KEY, context.getObject(UserDeliveryInitiation.class));
        COMMANDS.put(USER_USER_DELIVERY_REQUEST_CONFIRM_KEY, context.getObject(UserDeliveryPay.class));
        COMMANDS.put(USER_DELIVERS_TO_GET_KEY, context.getObject(UserDeliveryGet.class));
        COMMANDS.put(USER_USER_STATISTIC_KEY, context.getObject(UserStatistic.class));
    }

    private CommandContext() {
    }

    public static ActionCommand defineCommand(String path) {
        return COMMANDS.getOrDefault(path, context.getObject(EmptyCommand.class));
    }
}
