package service;


import db.dao.UserDao;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;
import entity.RoleType;
import entity.User;
import exeptions.NoSuchUserException;
import exeptions.OccupiedLoginException;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final PasswordEncoderService passwordEncoderService;
    private final UserDao userDao;

    public UserService(PasswordEncoderService passwordEncoderService, UserDao userDao) {
        this.passwordEncoderService = passwordEncoderService;
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException {
        return userDao.findByEmailAndPasswordWithPermissions(loginInfoDto.getUsername(),
                passwordEncoderService.encode(loginInfoDto.getPassword()))
                .orElseThrow(NoSuchUserException::new);
    }


    public void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {
        User user = getMapper().mapToEntity(registrationInfoDto);
        try {
            userDao.save(user);
        } catch (SQLException e) {
            throw new OccupiedLoginException();
        }
    }

    //@Transactional
    public User replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException, SQLException {
        User user = userDao.findById(userId).orElseThrow(NoSuchUserException::new);
        user.setUserMoneyInCents(user.getUserMoneyInCents() + amountMoney);
        return userDao.save(user);
    }

    private EntityMapper<User, RegistrationInfoDto> getMapper() {
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
