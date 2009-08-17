package com.cve.web;

import com.cve.db.sample.SampleServer;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import java.io.IOException;


/**
 * For launching a DBBrowser server.
 * This currently uses the Grizzly servlet engine.
 */
public final class Main {

    public static void main(String[] args) {
        try {
            loadServers();
            startGrizzly();
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Exiting");
            System.exit(0);
        }
    }

    static void startGrizzly() throws IOException {
        ServletAdapter adapter = new ServletAdapter(new RequestRouterServlet());
        GrizzlyWebServer    server = new GrizzlyWebServer(8888,"/");
        server.addGrizzlyAdapter(adapter,new String[] {"/"});
        server.start();
    }

    static void loadServers() {
        SampleServer.load();
    }
}

