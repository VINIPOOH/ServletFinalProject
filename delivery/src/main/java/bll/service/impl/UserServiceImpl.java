package bll.service.impl;


import bll.exeptions.NoSuchUserException;
import bll.service.PasswordEncoderService;
import bll.service.UserService;
import dal.dao.UserDao;
import dal.entity.User;
import dal.exeptions.OccupiedLoginException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.dto.LoginInfoDto;
import web.dto.RegistrationInfoDto;

public class UserServiceImpl implements UserService {
    private static Logger log = LogManager.getLogger(UserService.class);

    private final PasswordEncoderService passwordEncoderService;
    private final UserDao userDao;

    public UserServiceImpl(PasswordEncoderService passwordEncoderService, UserDao userDao) {
        log.debug("created");

        this.passwordEncoderService = passwordEncoderService;
        this.userDao = userDao;
    }


    @Override
    public User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException {
        log.debug("loginInfoDto -"+loginInfoDto);

        return userDao.findByEmailAndPasswordWithPermissions(loginInfoDto.getUsername(),
                passwordEncoderService.encode(loginInfoDto.getPassword()))
                .orElseThrow(NoSuchUserException::new);
    }

    @Override
    public void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {
        log.debug("registrationInfoDto -"+registrationInfoDto);

        userDao.save(registrationInfoDto.getUsername(), passwordEncoderService.encode(registrationInfoDto.getPassword()));

    }

    @Override
    public void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException {
        log.debug("userId -"+userId+" amountMoney -"+amountMoney);

        userDao.replenishUserBalance(userId, amountMoney);
    }
}
