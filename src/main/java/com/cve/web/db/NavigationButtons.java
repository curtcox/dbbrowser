package com.cve.web.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.ui.UISearchBox;
import com.cve.util.URIs;
import com.cve.web.Icons;
import java.net.URI;

/**
 *
 * @author curt
 */
public final class NavigationButtons {

    static String ADD_SERVER = addServer();

    static String REMOVE_SERVER = removeServer();

    static String SEARCH = search("");

    static String SHUTDOWN = shutdown();

    static String LOGIN = login();

    static String LOGOUT = logout();

    static String addServer() {
        String tip = "Add a database server";
        Label text = Label.of("+");
        URI target = URIs.of("add");
        URI  image = Icons.PLUS;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    static String removeServer() {
        String tip = "Remove a database server";
        Label text = Label.of("-");
        URI target = URIs.of("remove");
        URI  image = Icons.MINUS;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    /**
     * Return a search box
     */
    static String search(String target) {
        return UISearchBox.contents(target).toString();
    }

    static String shutdown() {
        String tip = "Shutdown DBBrowser";
        Label text = Label.of("X");
        URI target = URIs.of("exit");
        URI  image = Icons.OFF;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    static String login() {
        return Link.textTarget(Label.of("login"), URIs.of("login")).toString();
    }

    static String logout() {
        return Link.textTarget(Label.of("logout"), URIs.of("logout")).toString();
    }

}
