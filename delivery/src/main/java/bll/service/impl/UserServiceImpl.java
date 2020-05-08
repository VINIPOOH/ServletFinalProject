package bll.service.impl;


import bll.exeptions.NoSuchUserException;
import bll.service.PasswordEncoderService;
import bll.service.UserService;
import dal.dao.UserDao;
import dal.entity.User;
import dal.exeptions.OccupiedLoginException;
import web.dto.LoginInfoDto;
import web.dto.RegistrationInfoDto;

public class UserServiceImpl implements UserService {

    private final PasswordEncoderService passwordEncoderService;
    private final UserDao userDao;

    public UserServiceImpl(PasswordEncoderService passwordEncoderService, UserDao userDao) {
        this.passwordEncoderService = passwordEncoderService;
        this.userDao = userDao;
    }


    @Override
    public User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException {
        return userDao.findByEmailAndPasswordWithPermissions(loginInfoDto.getUsername(),
                passwordEncoderService.encode(loginInfoDto.getPassword()))
                .orElseThrow(NoSuchUserException::new);
    }

    @Override
    public void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {
        userDao.save(registrationInfoDto.getUsername(), passwordEncoderService.encode(registrationInfoDto.getPassword()));

    }

    @Override
    public void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException {
        userDao.replenishUserBalance(userId, amountMoney);
    }
}
