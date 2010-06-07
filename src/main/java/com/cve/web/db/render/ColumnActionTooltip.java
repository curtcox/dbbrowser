package com.cve.web.db.render;

import com.cve.model.db.DBColumn;
import com.cve.model.db.Order.Direction;
import com.cve.html.SimpleTooltip;
import com.cve.html.Tooltip;
import com.cve.lang.URIObject;
import com.cve.ui.UICascadingMenu;
import com.cve.ui.UIElement;
import com.cve.ui.UILink;
import com.cve.web.core.Icons;
import com.cve.web.db.SelectBuilderAction;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * For producing the tooltip attached to a column action cell.
 * @author Curt
 */
final class ColumnActionTooltip {

    static Tooltip columnDirection(DBColumn column, Direction direction) {
        List actions = Lists.newArrayList();
        actions.add(hide(column));
        if (direction!= Direction.ASC) {
            actions.add(asc(column));
        }
        if (direction!= Direction.DESC) {
            actions.add(desc(column));
        }
        if (direction!= Direction.NONE) {
            actions.add(none(column));
        }
        String html = UICascadingMenu.of(actions).toString();
        return SimpleTooltip.of(html);
    }

    static UIElement hide(DBColumn column) {
        String text   = "hide " + column.fullName();
        URIObject    target = SelectBuilderAction.HIDE.withArgs(column.fullName());
        URIObject     image = Icons.HIDE;
        UILink   link = UILink.textTargetImageAlt(text, target,image,text);
        return link;
    }

    static UIElement asc(DBColumn column) {
        String text   = "sort " + column.fullName();
        URIObject    target = SelectBuilderAction.ORDER.withArgs(column.fullName() + "=ASC");
        URIObject     image = Icons.SORT_ASC;
        UILink   link = UILink.textTargetImageAlt(text, target,image,text);
        return link;
    }

    static UIElement desc(DBColumn column) {
        String text   = "sort " + column.fullName();
        URIObject    target = SelectBuilderAction.ORDER.withArgs(column.fullName() + "=DESC");
        URIObject     image = Icons.SORT_DESC;
        UILink   link = UILink.textTargetImageAlt(text, target,image,text);
        return link;
    }

    static UIElement none(DBColumn column) {
        String text   = "sort " + column.fullName();
        URIObject    target = SelectBuilderAction.ORDER.withArgs(column.fullName() + "=NONE");
        URIObject     image = Icons.SORT_NONE;
        UILink   link = UILink.textTargetImageAlt(text, target,image,text);
        return link;
    }

}
