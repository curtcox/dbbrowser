package com.cve.launch;

import com.cve.db.dbio.DBMetaData;
import com.cve.db.sample.SampleServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.ServersStore;
import com.cve.stores.Stores;
import com.cve.stores.db.HintsStore;
import com.cve.util.URIs;
import com.cve.web.DefaultModelHtmlRenderers;
import com.cve.web.WebApp;
import java.awt.Desktop;
import java.io.IOException;


/**
 * For launching a DBBrowser server.
 * This currently uses the Grizzly servlet engine.
 */
public final class LaunchLocalServer {

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
        DBMetaData.Factory db = null;
        ServersStore serversStore = null;
        HintsStore hintsStore = null;
        ManagedFunction.Factory managedFunction = null;
        WebApp webApp = WebApp.of(
            LocalRequestHandler.of(serversStore,hintsStore,managedFunction).of(),
            DefaultModelHtmlRenderers.of(db,serversStore,hintsStore,managedFunction)
        );
        Grizzly.start(webApp, PORT);
    }

    static void loadServers() {
        SampleServer.load();
        Stores stores = null;
        ManagedFunction.Factory factory = null;
    }

    /**
     * Open a web browser to display the UI.
     */
    static void openBrowser() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URIs.of("http://localhost:" + PORT + "/"));
    }
}

