package bll.service;

import bll.service.impl.BillServiceImpl;
import bll.service.impl.DeliveryProcessServiceImpl;
import bll.service.impl.LocalityServiceImpl;
import bll.service.impl.PasswordEncoderServiceImpl;
import bll.service.impl.UserServiceImpl;
import dal.handling.JDBCDaoSingleton;
import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.dao.WayDao;
import dal.dao.impl.LocalityDao;

public class ServicesSingleton {

    private static final UserDao USER_DAO = JDBCDaoSingleton.getUserDao();
    private static final LocalityDao LOCALITY_DAO = JDBCDaoSingleton.getLocalityDao();
    private static final WayDao WAY_DAO = JDBCDaoSingleton.getWayDao();
    private static final DeliveryDao DELIVERY_DAO = JDBCDaoSingleton.getDeliveryDao();
    private static final BillDao BILL_DAO = JDBCDaoSingleton.getBillDao();


    private static final bll.service.PasswordEncoderService PASSWORD_ENCODER_SERVICE = new PasswordEncoderServiceImpl();
    private static final bll.service.UserService USER_SERVICE = new UserServiceImpl(PASSWORD_ENCODER_SERVICE, USER_DAO);
    private static final bll.service.LocalityService LOCALITY_SERVICE = new LocalityServiceImpl(LOCALITY_DAO);
    private static final bll.service.DeliveryProcessService DELIVERY_PROCESS_SERVICE = new DeliveryProcessServiceImpl(WAY_DAO, DELIVERY_DAO);
    private static final bll.service.BillService BILL_SERVICE = new BillServiceImpl(BILL_DAO);

    private ServicesSingleton() {
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
