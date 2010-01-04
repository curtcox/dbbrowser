package com.cve.launch;

import com.cve.web.RequestRouterServlet;
import com.cve.web.WebApp;
import java.io.IOException;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;

/**
 * For dealing with the Grizzly web server.
 * @author curt
 */
final class Grizzly {

    static void start(WebApp webApp,int port) throws IOException {
        ServletAdapter adapter = new ServletAdapter(
            RequestRouterServlet.of(webApp)
        );
        GrizzlyWebServer    server = new GrizzlyWebServer(port,"/");
        server.addGrizzlyAdapter(adapter,new String[] {"/"});
        server.start();
    }

}
