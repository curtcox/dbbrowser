package com.cve.web.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.ui.UISearchBox;
import com.cve.util.URIs;
import com.cve.web.Icons;
import com.cve.web.Search;
import java.net.URI;

/**
 * Navigation buttons that are used on lots of different pages.
 * @author curt
 */
public final class NavigationButtons {

    public static String ADD_SERVER = addServer();

    public static String REMOVE_SERVER = removeServer();

    public static String SEARCH = search(Search.EMPTY);

    public static String SHUTDOWN = shutdown();

    public static String LOGIN = login();

    public static String LOGOUT = logout();

    public static String addServer() {
        String tip = "Add a database server";
        Label text = Label.of("+");
        URI target = URIs.of("add");
        URI  image = Icons.PLUS;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    public static String removeServer() {
        String tip = "Remove a database server";
        Label text = Label.of("-");
        URI target = URIs.of("remove");
        URI  image = Icons.MINUS;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    /**
     * Return a search box
     */
    public static String search(Search search) {
        return UISearchBox.contents(search.target).toString();
    }

    public static String shutdown() {
        String tip = "Shutdown DBBrowser";
        Label text = Label.of("X");
        URI target = URIs.of("exit");
        URI  image = Icons.OFF;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    public static String login() {
        return Link.textTarget(Label.of("login"), URIs.of("login")).toString();
    }

    public static String logout() {
        return Link.textTarget(Label.of("logout"), URIs.of("logout")).toString();
    }

}
