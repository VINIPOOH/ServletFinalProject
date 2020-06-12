package web.comand.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static web.constant.PageConstance.ERROR_404_FILE_NAME;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;

@RunWith(MockitoJUnitRunner.class)
public class Error404Test {

    @InjectMocks
    Error404 error404;

    @Mock
    HttpServletRequest httpServletRequest;

    @Test
    public void execute() {
        String actual = error404.execute(httpServletRequest);

        assertEquals(MAIN_WEB_FOLDER + ERROR_404_FILE_NAME, actual);
    }
}