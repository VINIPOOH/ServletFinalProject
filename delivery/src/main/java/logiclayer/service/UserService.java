package logiclayer.service;

import dal.entity.User;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;
import dto.UserStatisticDto;
import logiclayer.exeption.NoSuchUserException;
import logiclayer.exeption.OccupiedLoginException;
import logiclayer.exeption.ToMachMoneyException;

import java.util.List;

public interface UserService {

    User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException;

    boolean addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException;

    boolean replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException, ToMachMoneyException;

    long getUserBalance(long userId);

    List<UserStatisticDto> getAllUsers();

}
