package com.cve.web.management.browsers;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableRow;
import com.cve.ui.UITable;
import com.cve.web.management.ObjectLink;
import com.cve.web.management.ObjectLinks;
import java.util.*;

/**
 * @author ccox
 */
public final class CollectionBrowser
    extends AbstractBrowser
{

    final Log log = Logs.of();

    private CollectionBrowser() {
        super(Collection.class);
        
    }

    public static CollectionBrowser of() {
        return new CollectionBrowser();
    }

    /* (non-Javadoc)
     */
    @Override
    public UIElement getComponentFor(Object o) {
        Collection c = (Collection) o;
        UITable table = UITable.of();
        int i = 0;
        for (Object value : c) {
            UITableRow row = UITableRow.of(UITableDetail.of("" + i),link(value));
            table = table.with(row);
            i++;
        }
        return table;
    }

    private UITableDetail link(Object o) {
        return UITableDetail.of(ObjectLinks.of().to("" + o,o));
    }
}
