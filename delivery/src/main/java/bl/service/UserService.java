package bl.service;

import bl.exeptions.NoSuchUserException;
import bl.exeptions.OccupiedLoginException;
import dal.entity.User;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;

public interface UserService {

    User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException;

    void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException;

    void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException;

    long getUserBalance(long userId);
}
