package com.cve.web;

import com.cve.db.sample.SampleServer;
import com.cve.stores.ServersStore;
import com.cve.util.URIs;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import java.awt.Desktop;
import java.io.IOException;


/**
 * For launching a DBBrowser server.
 * This currently uses the Grizzly servlet engine.
 */
public final class Main {

    static final int PORT = 8888;

    public static void main(String[] args) {
        try {
            loadServers();
            startGrizzly();
            openBrowser();
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Exiting");
            System.exit(0);
        }
    }

    static void startGrizzly() throws IOException {
        ServletAdapter adapter = new ServletAdapter(
            RequestRouterServlet.of(
                LocalRequestHandler.of(),
                DefaultModelHtmlRenderers.RENDERERS
            )
        );
        GrizzlyWebServer    server = new GrizzlyWebServer(PORT,"/");
        server.addGrizzlyAdapter(adapter,new String[] {"/"});
        server.start();
    }

    static void loadServers() {
        SampleServer.load();
        ServersStore.load();
    }

    static void openBrowser() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URIs.of("http://localhost:" + PORT + "/"));
    }
}

