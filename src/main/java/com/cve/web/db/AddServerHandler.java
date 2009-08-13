package com.cve.web.db;

import com.cve.db.ConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
import com.cve.stores.ServersStore;
import com.cve.util.URIs;
import com.cve.web.AbstractFormHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.google.common.collect.ImmutableMap;
import java.net.URI;

/**
 * For adding a server.
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
        return PageResponse.of(AddServerPage.SAMPLE);
    }

    @Override
    public PageResponse post(PageRequest request) {
        ImmutableMap<String,String> params = request.parameters;
        String         user = params.get(AddServerPage.USER);
        String     password = params.get(AddServerPage.PASSWORD);
        String          url = params.get(AddServerPage.URL);
        URI             uri = URIs.of(url);
        JDBCURL     jdbcurl = JDBCURL.uri(uri);
        Server       server = Server.uri(uri);
        ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcurl, user, password);
        if (ServersStore.getServers().contains(server)) {
            String message = "There is already a server for " + url;
            return PageResponse.of(
                AddServerPage.messageServerInfo(message, server, info)
            );
        }
        try {
            ServersStore.addServer(server, info);
            return PageResponse.newRedirect(server.linkTo().getTarget());
        } catch (RuntimeException e) {
            String message = e.getMessage();
            return PageResponse.of(
                AddServerPage.messageServerInfo(message, server, info)
            );
        }
    }

}
