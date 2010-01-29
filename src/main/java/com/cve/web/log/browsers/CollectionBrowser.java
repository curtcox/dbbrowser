package com.cve.web.log.browsers;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.log.AbstractBrowser;
import com.cve.web.log.ObjectLink;
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
    public String getComponentFor(Object o) {
        Collection c = (Collection) o;
        UITable table = UITable.of();
        int i = 0;
        for (Object value : c) {
            UIRow row = UIRow.of(UIDetail.of("" + i),link(value));
            table = table.with(row);
            i++;
        }
        return table.toString();
    }

    private UIDetail link(Object o) {
        return UIDetail.of(ObjectLink.of().to("" + o,o));
    }
}
