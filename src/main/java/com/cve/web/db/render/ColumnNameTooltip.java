package com.cve.web.db.render;

import com.cve.html.SimpleTooltip;
import com.cve.html.Label;
import com.cve.html.Tooltip;
import com.cve.html.HTML;
import com.cve.db.DBColumn;
import com.cve.db.Filter;
import com.cve.ui.UICascadingMenu;
import com.cve.ui.UIElement;
import com.cve.ui.UILink;
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
            UICascadingMenu.of(joinInfo(column,joins))
                .with(filterInfo(filters)
            ).toString();
        HTML html = HTML.of(info);
        return SimpleTooltip.of(html);
    }

    /**
     * HTML showing information about this column.
     */
    static HTML info(DBColumn column) {
        notNull(column);
        return HTML.of(column.name);
    }

    static UIElement joinInfo(DBColumn source, DBColumn dest) {
        notNull(source);
        notNull(dest);
        String text   = "join with " + dest.fullName();
        URI    target = SelectBuilderAction.JOIN.withArgs(
             source.fullName() + "=" + dest.fullName()
        );
        UILink link = UILink.textTarget(Label.of(text), target);
        return link;
    }

    static UIElement info(Filter filter) {
        notNull(filter);
        DBColumn column = filter.column;
        String value  = filter.value.toString();
        String text   = "show only " + value;
        URI    target = SelectBuilderAction.FILTER.withArgs(column.fullName() + "=" + value);
        UILink   link = UILink.textTarget(Label.of(text), target);
        return link;
    }

    static List<UIElement> joinInfo(final DBColumn column, final ImmutableList<DBColumn> joins) {
        notNull(joins);
        List<DBColumn> ordered = Lists.newArrayList(joins);
        Collections.sort(ordered, new ColumnProximityComparator(column));
        List<UIElement> list = Lists.newArrayList();
        // Only use the "best" joins
        for (int i=0; i<30 && i<ordered.size(); i++) {
            DBColumn dest = ordered.get(i);
            list.add(joinInfo(column,dest));
        }
        return list;
    }

    static List<UIElement> filterInfo(ImmutableList<Filter> filters) {
        notNull(filters);
        List<UIElement> list = Lists.newArrayList();
        for (Filter filter : filters) {
            list.add(info(filter));
        }
        return list;
    }
}
