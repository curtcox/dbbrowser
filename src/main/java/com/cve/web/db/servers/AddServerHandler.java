package com.cve.web.db.servers;

import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.DBServer;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import com.cve.web.core.handlers.AbstractFormHandler;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.google.common.collect.ImmutableMap;
import java.net.URI;

import static com.cve.web.db.servers.AddServerPage.*;
import static com.cve.util.Check.notNull;

/**
 * For adding a database server to the ServerStore.
 * @author Curt
 */
final class AddServerHandler extends AbstractFormHandler {

    final DBServersStore serversStore;

    final Log log = Logs.of();

    private AddServerHandler(DBServersStore serversStore) {
        this.serversStore = notNull(serversStore);
        
    }

    public static AddServerHandler of(DBServersStore serversStore) {
        return new AddServerHandler(serversStore);
    }

    @Override
    public boolean handles(String uri) {
        return uri.equals("/add");
    }

    @Override
    public PageResponse get(PageRequest request) {
        return PageResponse.of(request,SAMPLE);
    }

    @Override
    public PageResponse post(PageRequest request) {
        ImmutableMap<String,String> params = request.parameters;
        String         user = params.get(USER);
        String     password = params.get(PASSWORD);
        String          url = params.get(URL);
        String   serverName = params.get(SERVER);
        URI             uri = URIs.of(url);
        JDBCURL     jdbcurl = JDBCURL.uri(uri);
        DBServer       server = DBServer.uri(URIs.of(serverName));
        DBDriver       driver = null;
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcurl, user, password, driver);
        if (serversStore.keys().contains(server)) {
            String message = "There is already a server for " + url;
            return PageResponse.of(
                request,AddServerPage.messageServerUserPasswordJdbcUrl(message, server, user, password, url)
            );
        }
        try {
            serversStore.put(server, info);
            return PageResponse.newRedirect(request,server.linkTo().getTarget());
        } catch (RuntimeException e) {
            String message = e.getMessage();
            return PageResponse.of(
                request,AddServerPage.messageServerUserPasswordJdbcUrl(message, server, user, password, url)
            );
        }
    }

}
