package com.cve.launch;

import com.cve.db.dbio.DBMetaData;
import com.cve.db.dbio.LocalDBMetaDataFactory;
import com.cve.db.sample.SampleServer;
import com.cve.stores.LocalManagedFunctionFactory;
import com.cve.stores.LocalStoreFactory;
import com.cve.stores.ManagedFunction;
import com.cve.stores.Store;
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

    final ManagedFunction.Factory managedFunction;
    final DBMetaData.Factory db;
    final DBServersStore dbServersStore;
    final FSServersStore fsServersStore;
    final DBHintsStore hintsStore;
    final int PORT = PortFinder.findFree();

    private LaunchLocalServer(
        ManagedFunction.Factory managedFunction, DBMetaData.Factory db,
        DBServersStore dbServersStore, FSServersStore fsServersStore,
        DBHintsStore hintsStore)
    {
        this.managedFunction = managedFunction;
        this.db = db;
        this.dbServersStore = dbServersStore;
        this.fsServersStore = fsServersStore;
        this.hintsStore = hintsStore;
    }
    
    static LaunchLocalServer of() {
        Store.Factory stores = LocalStoreFactory.of();
        ManagedFunction.Factory managedFunction = LocalManagedFunctionFactory.of(stores);
        DBServersStore dbServersStore = stores.of(DBServersStore.class);
        FSServersStore fsServersStore = stores.of(FSServersStore.class);
        DBHintsStore       hintsStore = stores.of(DBHintsStore.class);
        DBMetaData.Factory         db = LocalDBMetaDataFactory.of(dbServersStore, managedFunction);
        return new LaunchLocalServer(
           managedFunction,
           db, dbServersStore, fsServersStore,
           hintsStore
        );
    }
    
    public static void main(String[] args) {
        try {
            launch();
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Exiting");
            System.exit(0);
        }
    }

    static void launch() throws IOException {
        LaunchLocalServer launcher = of();
        launcher.loadServers();
        launcher.startGrizzly();
        launcher.openBrowser();
    }

    void startGrizzly() throws IOException {
        WebApp webApp = WebApp.of(
            LocalRequestHandler.of(dbServersStore,fsServersStore,hintsStore,managedFunction),
            DefaultModelHtmlRenderers.of(db,dbServersStore,hintsStore,managedFunction)
        );
        Grizzly.start(webApp, PORT);
    }

    void loadServers() {
        SampleServer.of(dbServersStore, managedFunction);
        SampleServer.addToStore(dbServersStore);
    }

    /**
     * Open a web browser to display the UI.
     */
    void openBrowser() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URIs.of("http://localhost:" + PORT + "/"));
    }
}

