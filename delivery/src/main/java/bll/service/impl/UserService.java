package bll.service.impl;


import bll.dto.mapper.Mapper;
import dal.dao.UserDao;
import web.dto.LoginInfoDto;
import web.dto.RegistrationInfoDto;
import dal.entity.RoleType;
import dal.entity.User;
import exeptions.NoSuchUserException;
import exeptions.OccupiedLoginException;

import java.sql.SQLException;
import java.util.List;

public class UserService implements bll.service.UserService {

    private final PasswordEncoderService passwordEncoderService;
    private final UserDao userDao;

    public UserService(PasswordEncoderService passwordEncoderService, UserDao userDao) {
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
        User user = getMapper().map(registrationInfoDto);
        try {
            userDao.save(user);
        } catch (SQLException e) {
            throw new OccupiedLoginException();
        }
    }

    //@Transactional
    @Override
    public void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException {
        userDao.replenishUserBalance(userId, amountMoney);
    }

    private Mapper<RegistrationInfoDto, User> getMapper() {
        return (registration) -> User.builder()
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .email(registration.getUsername())
                .enabled(true)
                .userMoneyInCents(0L)
                .password(passwordEncoderService.encode(registration.getPassword()))
                .roleType(RoleType.ROLE_USER)
                .build();
    }
}
