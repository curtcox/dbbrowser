package com.cve.web.management.browsers;

import com.cve.ui.HTMLTags;
import com.cve.lang.AnnotatedClass;
import com.cve.lang.Strings;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableRow;
import com.cve.ui.UITable;
import com.cve.web.management.ObjectLink;
import com.cve.web.management.ObjectRegistry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * @author ccox
 */
public final class AnnotatedClassBrowser
    extends AbstractBrowser
{

    private AnnotatedClassBrowser() {
        super(AnnotatedClass.class);
    }

    public static AnnotatedClassBrowser of() {
        return new AnnotatedClassBrowser();
    }

    /* (non-Javadoc)
     */
    @Override
    public String getComponentFor(Object o) {
        AnnotatedClass c = (AnnotatedClass) o;
        UITable table = UITable.of();
        for (Object value : instancesOf(c)) {
            UITableRow row = UITableRow.of(link(value));
            table = table.with(row);
        }
        return table.toString();
    }

    ImmutableList instancesOf(AnnotatedClass c) {
        List list = Lists.newArrayList();
        for (Object o : ObjectRegistry.values()) {
            if (c.clazz.isInstance(o)) {
                list.add(o);
            }
        }
        return ImmutableList.copyOf(list);
    }

    private UITableDetail link(Object o) {
        ObjectLink link = ObjectLink.of();
        HTMLTags tags = HTMLTags.of();
        String string = tags.escape(Strings.first(150, "" + o));
        return UITableDetail.of(link.to(string,o));
    }
}
