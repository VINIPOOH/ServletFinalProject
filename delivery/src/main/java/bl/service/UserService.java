package bl.service;

import bl.exeptions.NoSuchUserException;
import bl.exeptions.OccupiedLoginException;
import dal.entity.User;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;
import dto.UserStatisticDto;

import java.util.List;

public interface UserService {

    User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException;

    boolean addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException;

    boolean replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException;

    long getUserBalance(long userId);

    List<UserStatisticDto> getAllUsers();

}
