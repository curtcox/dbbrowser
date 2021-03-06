package com.cve.web.core;

import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.RequestRouterServlet;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.WebApp;
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
        RequestHandler handler = mock(RequestHandler.class);
        ModelHtmlRenderer renderer = mock(ModelHtmlRenderer.class);
        WebApp webapp = WebApp.of(handler, renderer);
        RequestRouterServlet servlet = RequestRouterServlet.of(webapp);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = new PrintWriter(new ByteArrayOutputStream());
        when(request.getRequestURI()).thenReturn("/server/");
        when(response.getWriter()).thenReturn(printWriter);
        servlet.doGet(request, response);
    }
}
