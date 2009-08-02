package com.cve.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;


import static org.mockito.Mockito.*;
/**
 *
 * @author curt
 */
public class RequestRouterServletTest {

    @Test
    public void doGetServer() throws IOException {
        RequestRouterServlet servlet = new RequestRouterServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = new PrintWriter(new ByteArrayOutputStream());
        when(request.getRequestURI()).thenReturn("/server/");
        when(response.getWriter()).thenReturn(printWriter);
        servlet.doGet(request, response);
    }
}
