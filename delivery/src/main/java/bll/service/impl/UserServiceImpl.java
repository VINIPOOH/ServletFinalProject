package bll.service.impl;


import bll.service.PasswordEncoderService;
import dal.dao.UserDao;
import dal.entity.User;
import exeptions.NoSuchUserException;
import dal.exeptions.OccupiedLoginException;
import web.dto.LoginInfoDto;
import web.dto.RegistrationInfoDto;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements bll.service.UserService {

    private final PasswordEncoderService passwordEncoderService;
    private final UserDao userDao;

    public UserServiceImpl(PasswordEncoderService passwordEncoderService, UserDao userDao) {
        this.passwordEncoderService = passwordEncoderService;
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException {
        return userDao.findByEmailAndPasswordWithPermissions(loginInfoDto.getUsername(),
                passwordEncoderService.encode(loginInfoDto.getPassword()))
                .orElseThrow(NoSuchUserException::new);
    }

    @Override
    public void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {
        userDao.save(registrationInfoDto.getUsername(),passwordEncoderService.encode(registrationInfoDto.getPassword()));

    }

    @Override
    public void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException {
        userDao.replenishUserBalance(userId, amountMoney);
    }
}
