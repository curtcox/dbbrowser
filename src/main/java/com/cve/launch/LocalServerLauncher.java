package com.cve.launch;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.sample.db.SampleH2Server;
import com.cve.stores.LocalManagedFunctionFactory;
import com.cve.stores.LocalStoreFactory;
import com.cve.stores.ManagedFunction;
import com.cve.stores.MemoryStoreFactory;
import com.cve.stores.Store;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.fs.FSServersStore;
import com.cve.util.URIs;
import com.cve.web.core.renderers.DefaultModelHtmlRenderers;
import com.cve.web.core.WebApp;
import java.awt.Desktop;
import java.io.IOException;
import java.sql.SQLException;


/**
 * For launching a DBBrowser server.
 * This currently uses the Grizzly servlet engine.
 */
public final class LocalServerLauncher {

    final ManagedFunction.Factory managedFunction;
    final DBMetaData.Factory db;
    final DBServersStore dbServersStore;
    final FSServersStore fsServersStore;
    final DBHintsStore hintsStore;
    final int PORT = PortFinder.findFree();

    private LocalServerLauncher(
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
    
    static LocalServerLauncher of() {
        Store.Factory stores = LocalStoreFactory.of();
        ManagedFunction.Factory managedFunction = LocalManagedFunctionFactory.of(stores);
        DBServersStore dbServersStore = stores.of(DBServersStore.class);
        FSServersStore fsServersStore = stores.of(FSServersStore.class);
        DBHintsStore       hintsStore = stores.of(DBHintsStore.class);
        DBMetaData.Factory         db = LocalDBMetaDataFactory.of(dbServersStore, managedFunction);
        return new LocalServerLauncher(
           managedFunction,
           db, dbServersStore, fsServersStore,
           hintsStore
        );
    }

    static LocalServerLauncher test() {
        Store.Factory stores = MemoryStoreFactory.of();
        ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
        DBServersStore dbServersStore = stores.of(DBServersStore.class);
        FSServersStore fsServersStore = stores.of(FSServersStore.class);
        DBHintsStore       hintsStore = stores.of(DBHintsStore.class);
        Log                       log = Logs.of();
        DBMetaData.Factory         db = LocalDBMetaDataFactory.of(dbServersStore, managedFunction);
        return new LocalServerLauncher(
           managedFunction,
           db, dbServersStore, fsServersStore,
           hintsStore
        );
    }

    public static void main(String[] args) {
        try {
            launchTest();
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Exiting");
            System.exit(0);
        }
    }

    static void launch() throws IOException, SQLException {
        LocalServerLauncher launcher = of();
        launcher.loadServers();
        launcher.startGrizzly();
        launcher.openBrowser();
    }

    static void launchTest() throws IOException, SQLException {
        LocalServerLauncher launcher = test();
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

    void loadServers() throws SQLException, IOException {
        SampleH2Server.of();
        SampleH2Server.addToStore(dbServersStore);
    }

    /**
     * Open a web browser to display the UI.
     */
    void openBrowser() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URIs.of("http://localhost:" + PORT + "/"));
    }
}

