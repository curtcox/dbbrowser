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
 * Map-aware object browser.
 * @author ccox
 */
public final class MapBrowser
    extends AbstractBrowser
{

    final Log log = Logs.of();

    private MapBrowser() {
        super(Map.class);
        
    }

    public static MapBrowser of() {
        return new MapBrowser();
    }
    
    @Override
    public String getComponentFor(Object o) {
        Map map = (Map) o;
        UITable table = UITable.of();
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            UIRow row = UIRow.of(link(key),link(value));
            table = table.with(row);
        }
        return table.toString();
    }

    private UIDetail link(Object o) {
        return UIDetail.of(ObjectLink.of().to("" + o,o));
    }
}
