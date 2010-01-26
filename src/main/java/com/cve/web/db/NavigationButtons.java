package com.cve.web.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.ui.UISearchBox;
import com.cve.util.URIs;
import com.cve.web.Icons;
import com.cve.web.Search;
import java.net.URI;
import static com.cve.util.Check.notNull;

/**
 * Navigation buttons that are used on lots of different pages.
 * @author curt
 */
public final class NavigationButtons {

    final Log log;

    public final String ADD_SERVER = addServer();

    public final String REMOVE_SERVER = removeServer();

    public final String SEARCH = search(Search.EMPTY);

    public final String SHUTDOWN = shutdown();

    public final String LOGIN = login();

    public final String LOGOUT = logout();

    private NavigationButtons(Log log) {
        this.log = notNull(log);
    }

    public static NavigationButtons of(Log log) {
        return new NavigationButtons(log);
    }

    public String addServer() {
        String tip = "Add a database server";
        Label text = Label.of("+",log);
        URI target = URIs.of("add");
        URI  image = Icons.PLUS;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    public String removeServer() {
        String tip = "Remove a database server";
        Label text = Label.of("-",log);
        URI target = URIs.of("remove");
        URI  image = Icons.MINUS;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    /**
     * Return a search box
     */
    public String search(Search search) {
        return UISearchBox.contents(search,log).toString();
    }

    public String shutdown() {
        String tip = "Shutdown DBBrowser";
        Label text = Label.of("X",log);
        URI target = URIs.of("exit");
        URI  image = Icons.OFF;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    public String login() {
        return Link.textTarget(Label.of("login",log), URIs.of("login")).toString();
    }

    public String logout() {
        return Link.textTarget(Label.of("logout",log), URIs.of("logout")).toString();
    }

}
