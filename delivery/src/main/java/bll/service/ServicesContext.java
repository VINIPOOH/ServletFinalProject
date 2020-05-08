package bll.service;

import bll.service.impl.*;
import dal.control.JDBCDaoContext;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.dao.WayDao;
import dal.dao.impl.LocalityDao;

public class ServicesContext {

    private static final UserDao USER_DAO = JDBCDaoContext.getUserDao();
    private static final LocalityDao LOCALITY_DAO = JDBCDaoContext.getLocalityDao();
    private static final WayDao WAY_DAO = JDBCDaoContext.getWayDao();
    private static final DeliveryDao DELIVERY_DAO = JDBCDaoContext.getDeliveryDao();
    private static final BillDao BILL_DAO = JDBCDaoContext.getBillDao();


    private static final bll.service.PasswordEncoderService PASSWORD_ENCODER_SERVICE = new PasswordEncoderServiceImpl();
    private static final bll.service.UserService USER_SERVICE = new UserServiceImpl(PASSWORD_ENCODER_SERVICE, USER_DAO);
    private static final bll.service.LocalityService LOCALITY_SERVICE = new LocalityServiceImpl(LOCALITY_DAO);
    private static final bll.service.DeliveryProcessService DELIVERY_PROCESS_SERVICE = new DeliveryProcessServiceImpl(WAY_DAO, DELIVERY_DAO);
    private static final bll.service.BillService BILL_SERVICE = new BillServiceImpl(BILL_DAO, USER_DAO, DELIVERY_DAO);

    private ServicesContext() {
    }

    public static bll.service.PasswordEncoderService getPasswordEncoderService() {
        return PASSWORD_ENCODER_SERVICE;
    }

    public static bll.service.UserService getUserService() {
        return USER_SERVICE;
    }

    public static bll.service.LocalityService getLocalityService() {
        return LOCALITY_SERVICE;
    }

    public static bll.service.DeliveryProcessService getDeliveryProcessService() {
        return DELIVERY_PROCESS_SERVICE;
    }

    public static bll.service.BillService getBillService() {
        return BILL_SERVICE;
    }


}