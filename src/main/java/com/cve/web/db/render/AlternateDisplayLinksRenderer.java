package com.cve.web.db.render;

import com.cve.db.Select;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.db.SelectResults;
import com.cve.db.select.URIRenderer;
import com.cve.stores.ServersStore;
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

    final ServersStore serversStore;

    private AlternateDisplayLinksRenderer(SelectResults results, ServersStore serversStore) {
        this.select  = notNull(results.select);
        this.search  = notNull(results.search);
        this.serversStore = serversStore;
    }

    static AlternateDisplayLinksRenderer results(SelectResults results, ServersStore serversStore) {
        return new AlternateDisplayLinksRenderer(results,serversStore);
    }

    public static String render(SelectResults results, ServersStore serversStore) {
        AlternateDisplayLinksRenderer renderer = new AlternateDisplayLinksRenderer(results,serversStore);
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
        URI  target = URIs.of( "/view/" + view + URIRenderer.render(select,search));
        String tip = view.name();
        URI   image = view.icon;
        return Link.textTargetImageAlt(text, target, image,tip).toString();
    }

    /**
     * Create a link to the given view.
     */
    String viewSQLLink() {
        Label  text = Label.of("SQL");
        URI target = FreeFormQueryHandler.of(serversStore).linkTo(select,search);
        String tip = SQL.name();
        URI   image = SQL.icon;
        return Link.textTargetImageAlt(text, target, image, tip).toString();
    }

    /**
     * Create a link to the given view.
     */
    String viewZLink() {
        Label  text = Label.of("/z/");
        URI  target = CompressedURIHandler.shortURI(URIRenderer.render(select,search));
        String tip = "Compressed URL";
        URI   image = COMPRESSED.icon;
        return Link.textTargetImageAlt(text, target, image, tip).toString();
    }

}
