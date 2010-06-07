package com.cve.web.db.render;

import com.cve.lang.URIObject;
import com.cve.model.db.DBLimit;
import com.cve.web.db.SelectBuilderAction;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.SelectResults;
import com.cve.web.core.Icons;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For displaying paging widgets (next, back, etc..) for paging through a
 * big result set.
 */
@Immutable
public final class PagingLinksRenderer {

    final Log log = Logs.of();
    /**
     * The results we render
     */
    private final SelectResults results;

    public static final String NEXT = "Next";

    public static final String BACK = "Back";

    public static final String BIGGER = "More Rows";

    public static final String SMALLER = "Fewer Rows";

    private PagingLinksRenderer(SelectResults results) {
        this.results = notNull(results);
    }

    static PagingLinksRenderer results(SelectResults results) {
        return new PagingLinksRenderer(results);
    }

    public static String render(SelectResults results) {
        PagingLinksRenderer renderer = new PagingLinksRenderer(results);
        return renderer.pagingLinks();
   }

    String pagingLinks() {
        StringBuilder out = new StringBuilder();
        DBLimit limit = results.select.limit;
        if (limit.offset>0) {
            Label  text = Label.of(BACK);
            URIObject  target = SelectBuilderAction.BACK.withArgs("1");
            String  tip = "Previous rows";
            URIObject   image = Icons.BACK;
            out.append(Link.textTargetImageAlt(text, target,image,tip) + " ");
        }
        if (results.hasMore) {
            Label  text = Label.of(NEXT);
            URIObject  target = SelectBuilderAction.NEXT.withArgs("1");
            String  tip = "Next rows";
            URIObject   image = Icons.NEXT;
            out.append(Link.textTargetImageAlt(text, target, image, tip) + " ");
        }
        if (limit.limit > 20) {
            Label  text = Label.of(SMALLER);
            URIObject  target = SelectBuilderAction.SMALLER.withArgs("10");
            String  tip = "Fewer rows";
            URIObject   image = Icons.MINUS;
            out.append(Link.textTargetImageAlt(text, target, image, tip) + " ");
        }
        if (results.hasMore) {
            Label  text = Label.of(BIGGER);
            URIObject  target = SelectBuilderAction.BIGGER.withArgs("10");
            String  tip = "More rows";
            URIObject   image = Icons.PLUS;
            out.append(Link.textTargetImageAlt(text, target, image, tip) + " ");
        }
        return out.toString();
    }

}
