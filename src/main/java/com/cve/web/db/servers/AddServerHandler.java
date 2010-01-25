package com.cve.web.db.servers;

import com.cve.log.Log;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.DBServer;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import com.cve.web.AbstractFormHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
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

    final Log log;

    private AddServerHandler(DBServersStore serversStore, Log log) {
        this.serversStore = notNull(serversStore);
        this.log = notNull(log);
    }

    public static AddServerHandler of(DBServersStore serversStore, Log log) {
        return new AddServerHandler(serversStore,log);
    }

    @Override
    public boolean handles(String uri) {
        return uri.equals("/add");
    }

    @Override
    public PageResponse get(PageRequest request) {
        return PageResponse.of(SAMPLE,null);
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
        DBServer       server = DBServer.uri(URIs.of(serverName),log);
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcurl, user, password);
        if (serversStore.keys().contains(server)) {
            String message = "There is already a server for " + url;
            return PageResponse.of(
                AddServerPage.messageServerUserPasswordJdbcUrl(message, server, user, password, url),log
            );
        }
        try {
            serversStore.put(server, info);
            return PageResponse.newRedirect(server.linkTo().getTarget(),log);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            return PageResponse.of(
                AddServerPage.messageServerUserPasswordJdbcUrl(message, server, user, password, url),log
            );
        }
    }

}
