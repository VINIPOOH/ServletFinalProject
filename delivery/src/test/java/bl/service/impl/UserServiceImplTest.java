package bl.service.impl;


import bl.exeption.NoSuchUserException;
import bl.exeption.OccupiedLoginException;
import bl.service.PasswordEncoderService;
import dal.dao.UserDao;
import dal.entity.RoleType;
import dal.entity.User;
import dal.exeption.AskedDataIsNotCorrect;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static bl.service.impl.ServisesTestConstant.getUserId;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {


    @InjectMocks
    UserServiceImpl userService;
    @Mock
    PasswordEncoderService passwordEncoderService;
    @Mock
    UserDao userDao;


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void loginUser() throws NoSuchUserException {
        String email = "email";
        String password = "password";
        doAnswer((invocation) -> Optional.of(User.builder()
                .email(invocation.getArgument(0))
                .password(invocation.getArgument(1))
                .build()))
                .when(userDao).findByEmailAndPasswordWithPermissions(anyString(), anyString());
        doAnswer((invocation) -> invocation.getArgument(0)).when(passwordEncoderService).encode(anyString());
        LoginInfoDto loginInfoDto = LoginInfoDto.builder()
                .password(password)
                .username(email)
                .build();

        User result = userService.loginUser(loginInfoDto);

        verify(userDao, times(1)).findByEmailAndPasswordWithPermissions(anyString(), anyString());
        verify(passwordEncoderService, times(1)).encode(anyString());
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
    }

    @Test(expected = NoSuchUserException.class)
    public void loginUserUserIsNotExist() throws NoSuchUserException {
        when(userDao.findByEmailAndPasswordWithPermissions(anyString(), anyString())).thenReturn(Optional.empty());

        doAnswer((invocation) -> invocation.getArgument(0)).when(passwordEncoderService).encode(anyString());
        LoginInfoDto loginInfoDto = LoginInfoDto.builder()
                .password("password")
                .username("email")
                .build();

        userService.loginUser(loginInfoDto);

        fail();
    }

    @Test
    public void addNewUserToDBAllCorrect() throws OccupiedLoginException, AskedDataIsNotCorrect {
        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDto();
        when(userDao.save(anyString(), anyString())).thenReturn(true);
        doAnswer((invocation) -> invocation.getArgument(0)).when(passwordEncoderService).encode(anyString());

        boolean result = userService.addNewUserToDB(registrationInfoDto);

        verify(userDao, times(1)).save(anyString(), anyString());
        assertTrue(result);
    }


    @Test(expected = OccupiedLoginException.class)
    public void addNewUserToDBOccupiedLogin() throws OccupiedLoginException, AskedDataIsNotCorrect {
        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDto();
        when(userDao.save(anyString(), anyString())).thenThrow(AskedDataIsNotCorrect.class);
        doAnswer((invocation) -> invocation.getArgument(0)).when(passwordEncoderService).encode(anyString());

        userService.addNewUserToDB(registrationInfoDto);

        fail();
    }

    @Test
    public void replenishAccountBalanceAllCorrect() throws NoSuchUserException, AskedDataIsNotCorrect {
        when(userDao.replenishUserBalance(anyLong(), anyLong())).thenReturn(true);

        boolean result = userService.replenishAccountBalance(getUserId(), 1L);

        verify(userDao, times(1)).replenishUserBalance(anyLong(), anyLong());
        assertTrue(result);
    }

    @Test(expected = NoSuchUserException.class)
    public void replenishAccountBalanceNoSuchUser() throws NoSuchUserException, AskedDataIsNotCorrect {
        when(userDao.replenishUserBalance(anyLong(), anyLong())).thenThrow(AskedDataIsNotCorrect.class);

        userService.replenishAccountBalance(getUserId(), 10);

        fail();
    }

    @Test
    public void getUserBalance() throws AskedDataIsNotCorrect {
        when(userDao.getUserBalanceByUserID(anyLong())).thenReturn((1L));

        long result = userService.getUserBalance(getUserId());

        verify(userDao, times(1)).getUserBalanceByUserID(anyLong());
        assertEquals(1, result);
    }

    @Test(expected = RuntimeException.class)
    public void getUserBalanceProblemsWithDb() throws AskedDataIsNotCorrect {
        when(userDao.getUserBalanceByUserID(anyLong())).thenThrow(AskedDataIsNotCorrect.class);

        userService.getUserBalance(getUserId());

        fail();
    }

    private RegistrationInfoDto getRegistrationInfoDto() {
        return RegistrationInfoDto.builder()
                .password("password")
                .passwordRepeat("password")
                .username("email")
                .build();
    }

    private User getUser(RegistrationInfoDto registrationInfoDto) {
        return User.builder()
                .id(0)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .email(registrationInfoDto.getUsername())
                .enabled(true)
                .userMoneyInCents(0L)
                .password(passwordEncoderService.encode(registrationInfoDto.getPassword()))
                .roleType(RoleType.ROLE_USER)
                .build();
    }
}