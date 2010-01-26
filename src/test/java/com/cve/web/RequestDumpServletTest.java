package com.cve.web;

import com.cve.log.Log;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;

import static org.mockito.Mockito.*;
/**
 *
 * @author curt
 */
public class RequestDumpServletTest {

    Log log;

    @Test
    public void handled() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[0]);
        RequestDumpServlet.of(log).tableOf(request);
    }
}
