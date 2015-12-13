package com.cve.launch;

import com.cve.web.core.RequestRouterServlet;
import com.cve.web.core.WebApp;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;

import java.io.IOException;

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
        System.out.println("Grizzly started");
    }

}
