package com.cve.launch;

import com.cve.db.dbio.DBMetaData;
import com.cve.db.sample.SampleServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.fs.FSServersStore;
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

    static final int PORT = PortFinder.findFree();

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
        DBServersStore dbServersStore = null;
        FSServersStore fsServersStore = null;
        DBHintsStore hintsStore = null;
        ManagedFunction.Factory managedFunction = null;
        WebApp webApp = WebApp.of(
            LocalRequestHandler.of(dbServersStore,fsServersStore,hintsStore,managedFunction),
            DefaultModelHtmlRenderers.of(db,dbServersStore,hintsStore,managedFunction)
        );
        Grizzly.start(webApp, PORT);
    }

    static void loadServers() {
        SampleServer.load();
    }

    /**
     * Open a web browser to display the UI.
     */
    static void openBrowser() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URIs.of("http://localhost:" + PORT + "/"));
    }
}

