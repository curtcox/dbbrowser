package com.cve.db.render;

import com.cve.html.SimpleTooltip;
import com.cve.html.Label;
import com.cve.html.Tooltip;
import com.cve.html.Link;
import com.cve.html.HTML;
import com.cve.db.DBColumn;
import com.cve.db.Filter;
import com.cve.web.db.SelectBuilderAction;
import com.google.common.collect.ImmutableList;

import com.google.common.collect.Lists;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import static com.cve.util.Check.notNull;

/**
 * For producing the tooltip attached to a column name cell.
 */
public final class ColumnNameTooltip
{

    public static Tooltip columnJoinsFilters(
        DBColumn column, ImmutableList<DBColumn> joins, ImmutableList<Filter> filters)
    {
        String info =
            info(column).toString() +
            table(
                joinInfo(column,joins).toString()  +
                filterInfo(filters).toString()
            );
        HTML html = HTML.of(info);
        return SimpleTooltip.of(html);
    }

    /**
     * HTML showing information about this column.
     */
    static HTML info(DBColumn column) {
        notNull(column);
        return HTML.of(column.getName());
    }

    static HTML joinInfo(DBColumn source, DBColumn dest) {
        notNull(source);
        notNull(dest);
        String text   = "join with " + dest.fullName();
        URI    target = SelectBuilderAction.JOIN.withArgs(
             source.fullName() + "=" + dest.fullName()
        );
        String   html = Link.textTarget(Label.of(text), target).toString();
        return HTML.of(html);
    }

    static HTML info(Filter filter) {
        notNull(filter);
        DBColumn column = filter.getColumn();
        String value  = filter.getValue().toString();
        String text   = "show only " + value;
        URI    target = SelectBuilderAction.FILTER.withArgs(column.fullName() + "=" + value);
        String   html = Link.textTarget(Label.of(text), target).toString();
        return HTML.of(html);
    }

    static HTML joinInfo(final DBColumn column, final ImmutableList<DBColumn> joins) {
        notNull(joins);
        List<DBColumn> ordered = Lists.newArrayList(joins);
        Collections.sort(ordered, new ColumnProximityComparator(column));
        StringBuilder out = new StringBuilder();
        // Only use the "best" joins
        for (int i=0; i<30 && i<ordered.size(); i++) {
            DBColumn dest = ordered.get(i);
            out.append(tr(td( joinInfo(column,dest).toString() )));
        }
        return HTML.of(out.toString());
    }

    static HTML filterInfo(ImmutableList<Filter> filters) {
        notNull(filters);
        StringBuilder out = new StringBuilder();
        for (Filter filter : filters) {
            out.append(tr(td( info(filter).toString()  )));
        }
        return HTML.of(out.toString());
    }

    // Use our own copies to make sure we don't get carriage returns.
    static String    td(String s) { return "<td>" + s + "</td>"; }
    static String    tr(String s) { return "<tr>" + s + "</tr>"; }
    static String table(String s) { return "<table border>" + s + "</table>"; }
}
