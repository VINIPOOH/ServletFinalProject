package bl.service;

import bl.service.impl.*;
import dal.control.JDBCDaoContext;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.dao.WayDao;
import dal.dao.impl.LocalityDao;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ServicesContext {

    private static final UserDao USER_DAO = JDBCDaoContext.getUserDao();
    private static final LocalityDao LOCALITY_DAO = JDBCDaoContext.getLocalityDao();
    private static final WayDao WAY_DAO = JDBCDaoContext.getWayDao();
    private static final DeliveryDao DELIVERY_DAO = JDBCDaoContext.getDeliveryDao();
    private static final BillDao BILL_DAO = JDBCDaoContext.getBillDao();
    private static final bl.service.PasswordEncoderService PASSWORD_ENCODER_SERVICE = new PasswordEncoderServiceImpl();
    private static final bl.service.UserService USER_SERVICE = new UserServiceImpl(PASSWORD_ENCODER_SERVICE, USER_DAO);
    private static final bl.service.LocalityService LOCALITY_SERVICE = new LocalityServiceImpl(LOCALITY_DAO);
    private static final bl.service.DeliveryProcessService DELIVERY_PROCESS_SERVICE = new DeliveryProcessServiceImpl(WAY_DAO, DELIVERY_DAO);
    private static final bl.service.BillService BILL_SERVICE = new BillServiceImpl(BILL_DAO, USER_DAO, DELIVERY_DAO);
    private static Logger log = LogManager.getLogger(ServicesContext.class);

    private ServicesContext() {
    }

    public static PasswordEncoderService getPasswordEncoderService() {
        log.debug("static initialized");

        return PASSWORD_ENCODER_SERVICE;
    }

    public static UserService getUserService() {
        log.debug("getUserService");

        return USER_SERVICE;
    }

    public static LocalityService getLocalityService() {
        log.debug("getLocalityService");

        return LOCALITY_SERVICE;
    }

    public static DeliveryProcessService getDeliveryProcessService() {
        log.debug("getDeliveryProcessService");

        return DELIVERY_PROCESS_SERVICE;
    }

    public static BillService getBillService() {
        log.debug("getBillService");

        return BILL_SERVICE;
    }


}
