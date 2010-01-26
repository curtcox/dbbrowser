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
 * Map-aware object browser.
 * @author ccox
 */
public final class MapBrowser
    extends AbstractBrowser
{

    final Log log;

    private MapBrowser(Log log) {
        super(Map.class);
        this.log = notNull(log);
    }

    public static MapBrowser of(Log log) {
        return new MapBrowser(log);
    }
    
    @Override
    public String getComponentFor(Object o) {
        Map map = (Map) o;
        UITable table = UITable.of(log);
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            UIRow row = UIRow.of(log,link(key),link(value));
            table = table.with(row);
        }
        return table.toString();
    }

    private UIDetail link(Object o) {
        return UIDetail.of(ObjectLink.to("" + o,o),log);
    }
}
