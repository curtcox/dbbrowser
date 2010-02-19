package com.cve.web.management.browsers;

import com.cve.log.Log;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableRow;
import com.cve.ui.UITable;
import com.cve.web.management.ObjectLink;
import com.google.common.collect.Multimap;
import static com.cve.util.Check.notNull;

/**
 * Map-aware object browser.
 * @author ccox
 */
public final class MultimapBrowser
    extends AbstractBrowser
{

    ;

    private MultimapBrowser() {
        super(Multimap.class);
        
    }

    public static MultimapBrowser of() {
        return new MultimapBrowser();
    }

    @Override
    public String getComponentFor(Object o) {
        Multimap map = (Multimap) o;
        UITable table = UITable.of();
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            UITableRow row = UITableRow.of(link(key),link(value));
            table = table.with(row);
        }
        return table.toString();
    }

    private UITableDetail link(Object o) {
        return UITableDetail.of(ObjectLink.of().to("" + o,o));
    }

}
