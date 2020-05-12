package bl.service.impl;


import bl.exeptions.NoSuchUserException;
import bl.exeptions.OccupiedLoginException;
import bl.service.PasswordEncoderService;
import bl.service.UserService;
import dal.dao.UserDao;
import dal.entity.User;
import dal.exeptions.AskedDataIsNotCorrect;
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
        log.debug("loginInfoDto -" + loginInfoDto);

        return userDao.findByEmailAndPasswordWithPermissions(loginInfoDto.getUsername(),
                passwordEncoderService.encode(loginInfoDto.getPassword()))
                .orElseThrow(NoSuchUserException::new);
    }

    @Override
    public void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {
        log.debug("registrationInfoDto -" + registrationInfoDto);

        try {
            userDao.save(registrationInfoDto.getUsername(), passwordEncoderService.encode(registrationInfoDto.getPassword()));
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            log.error("login is occupied", askedDataIsNotCorrect);

            throw new OccupiedLoginException();
        }

    }

    @Override
    public void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException {
        log.debug("userId -" + userId + " amountMoney -" + amountMoney);

        try {
            userDao.replenishUserBalance(userId, amountMoney);
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            log.error("no user", askedDataIsNotCorrect);

            throw new NoSuchUserException();
        }
    }

    @Override
    public long getUserBalance(long userId) {
        try {
            return userDao.getUserBalanceByUserID(userId);
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            log.error("Problems with db user must be correct", askedDataIsNotCorrect);
            throw new RuntimeException();
        }
    }
}
