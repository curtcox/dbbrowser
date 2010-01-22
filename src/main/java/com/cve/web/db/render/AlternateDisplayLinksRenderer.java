package com.cve.web.db.render;

import com.cve.model.db.Select;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.model.db.SelectResults;
import com.cve.io.db.select.DBURIRenderer;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import com.cve.web.CompressedURIHandler;
import com.cve.web.Search;
import com.cve.web.alt.AlternateView;
import com.cve.web.db.FreeFormQueryHandler;
import static com.cve.web.alt.AlternateView.*;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For rendering links to alternate displays of the given {@link SelectResult}S.
 */
@Immutable
public final class AlternateDisplayLinksRenderer {

    /**
     * What we provide links to alternate views for.
     */
    private final Select select;

    private final Search search;

    final DBServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    final Log log;

    private AlternateDisplayLinksRenderer(
        SelectResults results, DBServersStore serversStore, ManagedFunction.Factory managedFunction, Log log) {
                this.select  = notNull(results.select);
                this.search  = notNull(results.search);
           this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
        this.log = notNull(log);
    }

    static AlternateDisplayLinksRenderer results(SelectResults results, DBServersStore serversStore, ManagedFunction.Factory managedFunction, Log log) {
        return new AlternateDisplayLinksRenderer(results,serversStore,managedFunction,log);
    }

    public static String render(SelectResults results, DBServersStore serversStore, ManagedFunction.Factory managedFunction,Log log) {
        AlternateDisplayLinksRenderer renderer = new AlternateDisplayLinksRenderer(results,serversStore, managedFunction,log);
        return renderer.viewLinks();
   }

    String viewLinks() {
        return viewZLink() + " " +
               viewSQLLink() + " " +
               viewLink(CSV) + " " +
               viewLink(XLS) + " " +
               viewLink(XML) + " " +
               viewLink(JSON) + " " +
               viewLink(PDF);
    }

    /**
     * Create a link to the given view.
     */
    String viewLink(AlternateView view) {
        Label  text = Label.of(view.toString());
        URI  target = URIs.of( "/view/" + view + DBURIRenderer.render(select,search));
        String tip = view.name();
        URI   image = view.icon;
        return Link.textTargetImageAlt(text, target, image,tip).toString();
    }

    /**
     * Create a link to the given view.
     */
    String viewSQLLink() {
        Label  text = Label.of("SQL");
        URI target = FreeFormQueryHandler.of(serversStore,managedFunction,log).linkTo(select,search);
        String tip = SQL.name();
        URI   image = SQL.icon;
        return Link.textTargetImageAlt(text, target, image, tip).toString();
    }

    /**
     * Create a link to the given view.
     */
    String viewZLink() {
        Label  text = Label.of("/z/");
        URI  target = CompressedURIHandler.shortURI(DBURIRenderer.render(select,search));
        String tip = "Compressed URL";
        URI   image = COMPRESSED.icon;
        return Link.textTargetImageAlt(text, target, image, tip).toString();
    }

}
