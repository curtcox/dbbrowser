package com.cve.web.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UISearchBox;
import com.cve.util.URIs;
import com.cve.web.core.Icons;
import com.cve.web.core.Search;
import java.net.URI;

/**
 * Navigation buttons that are used on lots of different pages.
 * @author curt
 */
public final class NavigationButtons {

    final Log log = Logs.of();

    public final String ADD_SERVER;

    public final String REMOVE_SERVER;

    public final String SEARCH;

    public final String SHUTDOWN;

    public final String LOGIN;

    public final String LOGOUT;

    private NavigationButtons() {
        
        ADD_SERVER = addServer();
        REMOVE_SERVER = removeServer();
        SEARCH = search(Search.EMPTY);
        SHUTDOWN = shutdown();
        LOGIN = login();
        LOGOUT = logout();
    }

    public static NavigationButtons of() {
        return new NavigationButtons();
    }

    public String addServer() {
        String tip = "Add a database server";
        Label text = Label.of("+");
        URI target = URIs.of("add");
        URI  image = Icons.PLUS;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    public String removeServer() {
        String tip = "Remove a database server";
        Label text = Label.of("-");
        URI target = URIs.of("remove");
        URI  image = Icons.MINUS;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    /**
     * Return a search box
     */
    public String search(Search search) {
        return UISearchBox.contents(search).toString();
    }

    public String shutdown() {
        String tip = "Shutdown DBBrowser";
        Label text = Label.of("X");
        URI target = URIs.of("exit");
        URI  image = Icons.OFF;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    public String login() {
        return Link.textTarget(Label.of("login"), URIs.of("login")).toString();
    }

    public String logout() {
        return Link.textTarget(Label.of("logout"), URIs.of("logout")).toString();
    }

}
