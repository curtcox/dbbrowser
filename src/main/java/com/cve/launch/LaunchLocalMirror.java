package com.cve.launch;

import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.DefaultModelHtmlRenderers;
import com.cve.web.WebApp;
import java.io.IOException;


/**
 * For launching a DBBrowser server.
 * This currently uses the Grizzly servlet engine.
 */
public final class LaunchLocalMirror {

    static final int PORT = PortFinder.findFree();

    public static void main(String[] args) {
        try {
            startGrizzly();
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
        Log log = null;
        WebApp webApp = WebApp.of(
            LocalRequestHandler.of(dbServersStore,fsServersStore,hintsStore,managedFunction,log),
            DefaultModelHtmlRenderers.of(db,dbServersStore,hintsStore,managedFunction,log),
            log
        );
        Grizzly.start(webApp, PORT);
    }

}

