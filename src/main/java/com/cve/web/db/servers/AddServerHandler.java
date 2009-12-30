package com.cve.web.db.servers;

import com.cve.db.ConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
import com.cve.stores.Stores;
import com.cve.util.URIs;
import com.cve.web.AbstractFormHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.google.common.collect.ImmutableMap;
import java.net.URI;

import static com.cve.web.db.servers.AddServerPage.*;

/**
 * For adding a database server to the ServerStore.
 * @author Curt
 */
final class AddServerHandler extends AbstractFormHandler {

    AddServerHandler() {}

    @Override
    public boolean handles(String uri) {
        return uri.equals("/add");
    }

    @Override
    public PageResponse get(PageRequest request) {
        return PageResponse.of(SAMPLE);
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
        Server       server = Server.uri(URIs.of(serverName));
        ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcurl, user, password);
        if (Stores.getServerStore().getServers().contains(server)) {
            String message = "There is already a server for " + url;
            return PageResponse.of(
                AddServerPage.messageServerUserPasswordJdbcUrl(message, server, user, password, url)
            );
        }
        try {
            Stores.getServerStore().addServer(server, info);
            return PageResponse.newRedirect(server.linkTo().getTarget());
        } catch (RuntimeException e) {
            String message = e.getMessage();
            return PageResponse.of(
                AddServerPage.messageServerUserPasswordJdbcUrl(message, server, user, password, url)
            );
        }
    }

}
