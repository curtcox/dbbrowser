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
import java.io.IOException;
import java.sql.SQLException;


/**
 * For launching a DBBrowser server.
 */
public final class LocalSwingLauncher {

    final ManagedFunction.Factory managedFunction;
    final DBMetaData.Factory db;
    final DBServersStore dbServersStore;
    final FSServersStore fsServersStore;
    final DBHintsStore hintsStore;

    private LocalSwingLauncher(
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
    
    static LocalSwingLauncher of() {
        Store.Factory stores = LocalStoreFactory.of();
        ManagedFunction.Factory managedFunction = LocalManagedFunctionFactory.of(stores);
        DBServersStore dbServersStore = stores.of(DBServersStore.class);
        FSServersStore fsServersStore = stores.of(FSServersStore.class);
        DBHintsStore       hintsStore = stores.of(DBHintsStore.class);
        DBMetaData.Factory         db = LocalDBMetaDataFactory.of(dbServersStore, managedFunction);
        return new LocalSwingLauncher(
           managedFunction,
           db, dbServersStore, fsServersStore,
           hintsStore
        );
    }

    static LocalSwingLauncher test() {
        Store.Factory stores = MemoryStoreFactory.of();
        ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
        DBServersStore dbServersStore = stores.of(DBServersStore.class);
        FSServersStore fsServersStore = stores.of(FSServersStore.class);
        DBHintsStore       hintsStore = stores.of(DBHintsStore.class);
        Log                       log = Logs.of();
        DBMetaData.Factory         db = LocalDBMetaDataFactory.of(dbServersStore, managedFunction);
        return new LocalSwingLauncher(
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
        LocalSwingLauncher launcher = of();
        launcher.loadServers();
        Swing swing = launcher.startSwing();
        launcher.openBrowser(swing);
    }

    static void launchTest() throws IOException, SQLException {
        LocalSwingLauncher launcher = test();
        launcher.loadServers();
        Swing swing = launcher.startSwing();
        launcher.openBrowser(swing);
    }

    Swing startSwing() throws IOException {
        WebApp webApp = WebApp.of(
            LocalRequestHandler.of(dbServersStore,fsServersStore,hintsStore,managedFunction),
            DefaultModelHtmlRenderers.of(db,dbServersStore,hintsStore,managedFunction)
        );
        return Swing.start(webApp);
    }

    void loadServers() throws SQLException, IOException {
        SampleH2Server.of();
        SampleH2Server.addToStore(dbServersStore);
    }

    /**
     * Open a web browser to display the UI.
     */
    void openBrowser(Swing swing) throws IOException {
        swing.browse(URIs.of("/"));
    }
}

