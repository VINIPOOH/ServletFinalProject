package bll.service;

import dal.entity.User;
import exeptions.NoSuchUserException;
import exeptions.OccupiedLoginException;
import web.dto.LoginInfoDto;
import web.dto.RegistrationInfoDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException;

    void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException;

    //@Transactional
    void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException;
}
