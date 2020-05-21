package bl.service;

import bl.service.impl.*;
import dal.control.JDBCDaoContext;
import dal.dao.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ServicesContext {

    private static final UserDao USER_DAO = JDBCDaoContext.getObject(UserDao.class);
    private static final LocalityDao LOCALITY_DAO = JDBCDaoContext.getObject(LocalityDao.class);
    private static final WayDao WAY_DAO = JDBCDaoContext.getObject(WayDao.class);
    private static final DeliveryDao DELIVERY_DAO = JDBCDaoContext.getObject(DeliveryDao.class);
    private static final BillDao BILL_DAO = JDBCDaoContext.getObject(BillDao.class);
    private static final PasswordEncoderService PASSWORD_ENCODER_SERVICE = new PasswordEncoderServiceImpl();
    private static final UserService USER_SERVICE = new UserServiceImpl(PASSWORD_ENCODER_SERVICE, USER_DAO);
    private static final LocalityService LOCALITY_SERVICE = new LocalityServiceImpl(LOCALITY_DAO);
    private static final DeliveryProcessService DELIVERY_PROCESS_SERVICE = new DeliveryServiceImpl(WAY_DAO, DELIVERY_DAO);
    private static final BillService BILL_SERVICE = new BillServiceImpl(BILL_DAO, USER_DAO, DELIVERY_DAO);
    private static Logger log = LogManager.getLogger(ServicesContext.class);

    private static Map<Class, Object> contextMap;

    static {
        contextMap = new HashMap<>();
        contextMap.put(PasswordEncoderService.class, PASSWORD_ENCODER_SERVICE);
        contextMap.put(UserService.class, USER_SERVICE);
        contextMap.put(LocalityService.class, LOCALITY_SERVICE);
        contextMap.put(DeliveryProcessService.class, DELIVERY_PROCESS_SERVICE);
        contextMap.put(BillService.class, BILL_SERVICE);
    }

    private ServicesContext() {
    }

    public static <T> T getObject(Class<T> classType) {
        return (T) contextMap.get(classType);
    }


}
