package web.comand.action.impl;

import bl.exeption.NoSuchUserException;
import bl.service.UserService;
import dal.entity.User;
import dto.LoginInfoDto;
import dto.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static constants.TestConstant.getAdverser;
import static constants.TestConstant.getLocaleEn;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static web.constant.AttributeConstants.SESSION_LANG;
import static web.constant.PageConstance.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {
    @InjectMocks
    Login login;

    @Mock
    Validator loginDtoValidator;
    @Mock
    UserService userService;

    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    HttpSession session;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Before
    public void setUp() throws Exception {
        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_LANG)).thenReturn(getLocaleEn());
        when(httpServletRequest.getParameter(USERNAME)).thenReturn("user");
        when(httpServletRequest.getParameter(PASSWORD)).thenReturn("password");
    }

    @Test
    public void performGet() {
        String actual = login.performGet(httpServletRequest);

        assertEquals(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME, actual);
    }

    @Test
    public void performPost() throws NoSuchUserException {
        User user = getAdverser();
        when(loginDtoValidator.isValid(any(HttpServletRequest.class))).thenReturn(true);
        when(userService.loginUser(any(LoginInfoDto.class))).thenReturn(user);

        String actual = login.performPost(httpServletRequest);

        verify(httpServletRequest, times(1)).getParameter(USERNAME);
        verify(httpServletRequest, times(1)).getParameter(PASSWORD);
        verify(httpServletRequest, times(0)).setAttribute(anyString(), any(Object.class));
        verify(httpServletRequest, times(1)).getSession();
        verify(loginDtoValidator, times(1)).isValid(any(HttpServletRequest.class));
        assertEquals(REDIRECT_COMMAND + USER_PROFILE_REQUEST_COMMAND, actual);
    }

    @Test
    public void performPostIncorrectInput() throws NoSuchUserException {
        User user = getAdverser();
        when(loginDtoValidator.isValid(any(HttpServletRequest.class))).thenReturn(false);
        when(userService.loginUser(any(LoginInfoDto.class))).thenReturn(user);

        String actual = login.performPost(httpServletRequest);

        verify(httpServletRequest, times(0)).getParameter(USERNAME);
        verify(httpServletRequest, times(0)).getParameter(PASSWORD);
        verify(httpServletRequest, times(1)).setAttribute(anyString(), any(Object.class));
        verify(httpServletRequest, times(0)).getSession();
        verify(loginDtoValidator, times(1)).isValid(any(HttpServletRequest.class));
        assertEquals(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME, actual);
    }

    @Test
    public void performPostIncorrectData() throws NoSuchUserException {
        User user = getAdverser();
        when(loginDtoValidator.isValid(any(HttpServletRequest.class))).thenReturn(false);
        when(userService.loginUser(any(LoginInfoDto.class))).thenThrow(NoSuchUserException.class);

        String actual = login.performPost(httpServletRequest);

        verify(httpServletRequest, times(0)).getParameter(USERNAME);
        verify(httpServletRequest, times(0)).getParameter(PASSWORD);
        verify(httpServletRequest, times(1)).setAttribute(anyString(), any(Object.class));
        verify(httpServletRequest, times(0)).getSession();
        verify(loginDtoValidator, times(1)).isValid(any(HttpServletRequest.class));
        assertEquals(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME, actual);
    }

}