package com.cve.web.db.render;

import com.cve.lang.URIObject;
import com.cve.model.db.Select;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.model.db.SelectResults;
import com.cve.io.db.select.DBURIRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import com.cve.web.core.handlers.CompressedURIHandler;
import com.cve.web.core.Search;
import com.cve.web.alt.AlternateView;
import com.cve.web.db.FreeFormQueryHandler;
import static com.cve.web.alt.AlternateView.*;

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

    final Log log = Logs.of();

    private AlternateDisplayLinksRenderer(
        SelectResults results, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
                this.select  = notNull(results.select);
                this.search  = notNull(results.search);
           this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
        
    }

    static AlternateDisplayLinksRenderer results(SelectResults results, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new AlternateDisplayLinksRenderer(results,serversStore,managedFunction);
    }

    public static String render(SelectResults results, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        AlternateDisplayLinksRenderer renderer = new AlternateDisplayLinksRenderer(results,serversStore, managedFunction);
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
        URIObject  target = URIs.of( "/view/" + view + DBURIRenderer.render(select,search));
        String tip = view.name();
        URIObject   image = view.icon;
        return Link.textTargetImageAlt(text, target, image,tip).toString();
    }

    /**
     * Create a link to the given view.
     */
    String viewSQLLink() {
        Label  text = Label.of("SQL");
        URIObject target = FreeFormQueryHandler.of(serversStore,managedFunction).linkTo(select,search);
        String tip = SQL.name();
        URIObject   image = SQL.icon;
        return Link.textTargetImageAlt(text, target, image, tip).toString();
    }

    /**
     * Create a link to the given view.
     */
    String viewZLink() {
        Label  text = Label.of("/z/");
        URIObject  target = CompressedURIHandler.shortURI(DBURIRenderer.render(select,search));
        String tip = "Compressed URL";
        URIObject   image = COMPRESSED.icon;
        return Link.textTargetImageAlt(text, target, image, tip).toString();
    }

}
