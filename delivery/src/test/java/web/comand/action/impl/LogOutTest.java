package web.comand.action.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static web.constant.PageConstance.LOGIN_REQUEST_COMMAND;
import static web.constant.PageConstance.REDIRECT_COMMAND;

@RunWith(MockitoJUnitRunner.class)
public class LogOutTest {
    @InjectMocks
    LogOut logOut;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    private HttpSession session;

    @Before
    public void setUp() {
        when(httpServletRequest.getSession()).thenReturn(session);
    }

    @Test
    public void execute() {
        String actual = logOut.execute(httpServletRequest);

        verify(httpServletRequest, times(1)).getSession();
        verify(session, times(1)).invalidate();
        assertEquals(REDIRECT_COMMAND + LOGIN_REQUEST_COMMAND, actual);
    }
}