package com.cve.web.log.browsers;

import com.cve.log.Log;
import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.log.AbstractBrowser;
import com.cve.web.log.ObjectLink;
import java.util.*;
import static com.cve.util.Check.notNull;

/**
 * @author ccox
 */
public final class CollectionBrowser
    extends AbstractBrowser
{

    final Log log;

    private CollectionBrowser(Log log) {
        super(Collection.class);
        this.log = notNull(log);
    }

    public CollectionBrowser of(Log log) {
        return new CollectionBrowser(log);
    }

    /* (non-Javadoc)
     */
    @Override
    public String getComponentFor(Object o) {
        Collection c = (Collection) o;
        UITable table = UITable.of(log);
        int i = 0;
        for (Object value : c) {
            UIRow row = UIRow.of(log,UIDetail.of("" + i,log),link(value));
            table = table.with(row);
            i++;
        }
        return table.toString();
    }

    private UIDetail link(Object o) {
        return UIDetail.of(ObjectLink.of(log).to("" + o,o),log);
    }
}
