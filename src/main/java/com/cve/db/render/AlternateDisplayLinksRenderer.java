package com.cve.db.render;

import com.cve.db.Select;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.db.SelectResults;
import com.cve.db.select.URIRenderer;
import com.cve.html.SimpleTooltip;
import com.cve.html.Tooltip;
import com.cve.util.URIs;
import com.cve.web.Icons;
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

    private AlternateDisplayLinksRenderer(SelectResults results) {
        this.select  = notNull(results.select);
    }

    static AlternateDisplayLinksRenderer results(SelectResults results) {
        return new AlternateDisplayLinksRenderer(results);
    }

    public static String render(SelectResults results) {
        AlternateDisplayLinksRenderer renderer = new AlternateDisplayLinksRenderer(results);
        return renderer.viewLinks();
   }

    String viewLinks() {
        return viewSQLLink() + " " +
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
        URI  target = URIs.of( "/view/" + view + URIRenderer.render(select));
        String tip = view.name();
        URI   image = view.icon;
        return Link.textTargetImageAlt(text, target, image,tip).toString();
    }

    /**
     * Create a link to the given view.
     */
    String viewSQLLink() {
        Label  text = Label.of("SQL");
        URI target = FreeFormQueryHandler.linkTo(select);
        String tip = SQL.name();
        URI   image = SQL.icon;
        return Link.textTargetImageAlt(text, target, image, tip).toString();
    }

}
