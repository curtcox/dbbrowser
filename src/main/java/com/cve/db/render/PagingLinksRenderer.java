package com.cve.db.render;

import com.cve.db.Limit;
import com.cve.web.db.SelectBuilderAction;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.db.SelectResults;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For displaying paging widgets (next, back, etc..) for paging through a
 * big result set.
 */
@Immutable
public final class PagingLinksRenderer {

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
        Limit limit = results.getSelect().getLimit();
        if (limit.getOffset()>0) {
            Label  text = Label.of(BACK);
            URI  target = SelectBuilderAction.BACK.withArgs("1");
            out.append(Link.textTarget(text, target) + " ");
        }
        if (results.hasMore()) {
            Label  text = Label.of(NEXT);
            URI  target = SelectBuilderAction.NEXT.withArgs("1");
            out.append(Link.textTarget(text, target) + " ");
        }
        if (limit.getLimit() > 20) {
            Label  text = Label.of(SMALLER);
            URI  target = SelectBuilderAction.SMALLER.withArgs("10");
            out.append(Link.textTarget(text, target) + " ");
        }
        if (results.hasMore()) {
            Label  text = Label.of(BIGGER);
            URI  target = SelectBuilderAction.BIGGER.withArgs("10");
            out.append(Link.textTarget(text, target) + " ");
        }
        return out.toString();
    }

}
