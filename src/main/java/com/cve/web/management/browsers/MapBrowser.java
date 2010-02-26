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
    public UIElement getComponentFor(Object o) {
        Map map = (Map) o;
        UITable table = UITable.of();
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            UITableRow row = UITableRow.of(link(key),link(value));
            table = table.with(row);
        }
        return table;
    }

    private UITableDetail link(Object o) {
        return UITableDetail.of(ObjectLinks.of().to("" + o,o));
    }
}
