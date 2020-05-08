package bll.service;

import bll.exeptions.NoSuchUserException;
import dal.entity.User;
import dal.exeptions.OccupiedLoginException;
import web.dto.LoginInfoDto;
import web.dto.RegistrationInfoDto;

public interface UserService {

    User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException;

    void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException;

    void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException;
}
