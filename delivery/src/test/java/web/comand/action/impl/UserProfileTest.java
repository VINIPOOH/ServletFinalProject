package web.comand.action.impl;

import bl.exeption.NoSuchUserException;
import bl.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static constants.TestConstant.getAdverser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static web.constant.PageConstance.*;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileTest {
    @InjectMocks
    UserProfile userProfile;
    @Mock
    UserService userService;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    HttpSession session;

    private static final String MONEY = "money";

    @Before
    public void setUp() throws Exception {
        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(getAdverser());
        when(httpServletRequest.getParameter(MONEY)).thenReturn("1");
    }

    @Test
    public void performGet() {
        String actual = userProfile.performGet(httpServletRequest);

        assertEquals(MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME, actual);
    }

    @Test
    public void performPost() throws NoSuchUserException {
        when(userService.replenishAccountBalance(anyLong(), anyLong())).thenReturn(true);

        String actual = userProfile.performPost(httpServletRequest);

        verify(httpServletRequest, times(2)).getParameter(MONEY);
        verify(httpServletRequest, times(1)).setAttribute(anyString(), any(Object.class));
        verify(httpServletRequest, times(1)).getSession();
        assertEquals(MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME, actual);
    }

    @Test
    public void performPostInputMoneyZero() throws NoSuchUserException {
        when(httpServletRequest.getParameter(MONEY)).thenReturn("0");

        String actual = userProfile.performPost(httpServletRequest);

        verify(httpServletRequest, times(1)).getParameter(MONEY);
        verify(httpServletRequest, times(1)).setAttribute(anyString(), any(Object.class));
        assertEquals(MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME, actual);
    }

    @Test(expected = RuntimeException.class)
    public void performPostNoSuchUserException() throws NoSuchUserException {
        when(userService.replenishAccountBalance(anyLong(), anyLong())).thenThrow(NoSuchUserException.class);

        String actual = userProfile.performPost(httpServletRequest);

        fail();
    }
}